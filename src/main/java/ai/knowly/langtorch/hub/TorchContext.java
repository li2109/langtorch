package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.annotation.TorchHub;
import ai.knowly.langtorch.hub.annotation.TorchInject;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.domain.TorchContextConfig;
import ai.knowly.langtorch.hub.domain.TorchScope;
import ai.knowly.langtorch.hub.domain.TorchScopeValue;
import ai.knowly.langtorch.hub.domain.TorchletDefinition;
import ai.knowly.langtorch.hub.domain.TorchletDefinition.TorchletDefinitionBuilder;
import ai.knowly.langtorch.hub.exception.RequiredAnnotationNotFoundException;
import com.google.common.flogger.FluentLogger;
import com.google.mu.util.Optionals;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Torch context contains information about the torchlet and its dependencies. */
public class TorchContext {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final boolean verbose;

  private final ConcurrentHashMap<String, Object> singletonTorchlets;
  private final ConcurrentHashMap<String, TorchletDefinition> torchLetDefinitions;

  public TorchContext(TorchContextConfig torchContextConfig) {
    // Deconstruct the config.
    this.verbose = torchContextConfig.isVerbose();

    // Initialize the context.
    this.singletonTorchlets = new ConcurrentHashMap<>();
    this.torchLetDefinitions = new ConcurrentHashMap<>();

    if (verbose) {
      logger.atInfo().log("TorchContext Object constructed.\n");
    }
  }

  // Create a torchlet definition from a class.
  private static TorchletDefinition createTorchletDefinition(Class<?> aClass) {
    TorchletDefinitionBuilder torchletDef = TorchletDefinition.builder();
    torchletDef.setClazz(aClass);
    if (aClass.isAnnotationPresent(TorchScope.class)) {
      TorchScopeValue value = aClass.getDeclaredAnnotation(TorchScope.class).value();
      torchletDef.setScope(value);
    } else {
      torchletDef.setScope(TorchScopeValue.SINGLETON);
    }
    return torchletDef.build();
  }

  private static String generateComponentName(Class<?> aClass, Optional<String> userSpecifiedName) {
    if (userSpecifiedName.isPresent()) {
      return userSpecifiedName.get();
    }
    String className = aClass.getName();
    return String.format("%s$%s", className, userSpecifiedName.orElse(""));
  }

  public TorchContext run(Class<?> tochHubClass) {
    // Get the package name to scan.
    String toScanPackageName = getToScanPackageName(tochHubClass);
    // Scan the package and register the torchlet definitions.
    scanPackageAndRegisterTorchletDefinitions(toScanPackageName);
    // Create the singleton torchlets from the definitions.
    registerSingletonTorchlet();
    return this;
  }

  private void scanPackageAndRegisterTorchletDefinitions(String packageName) {
    ClassLoader classLoader = TorchContext.class.getClassLoader();
    URL resource = classLoader.getResource(packageNameToPath(packageName));
    File directory = new File(resource.getFile());
    scanDirectory(directory, packageName, classLoader);
  }

  private void scanDirectory(File directory, String packageName, ClassLoader classLoader) {
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          scanDirectory(file, String.format("%s.%s", packageName, file.getName()), classLoader);
        } else {
          registerComponent(packageName, classLoader, file);
        }
      }
    }
  }

  private void registerComponent(String packageName, ClassLoader classLoader, File file) {
    String className = file.getName().replace(".class", "");
    Class<?> aClass;
    try {
      aClass = classLoader.loadClass(packageName + "." + className);
    } catch (ClassNotFoundException e) {
      logger.atWarning().log(
          "Class not found: %s and fail to register into torch context.", className);
      return;
    }
    if (aClass.isAnnotationPresent(Torchlet.class)) {
      String torchletNameFromAnnotation = aClass.getDeclaredAnnotation(Torchlet.class).value();
      if (verbose) {
        logger.atInfo().log(String.format("Found Torchlet Class: %s", aClass.getName()) + "\n");
      }
      registerTorchletDefinition(aClass, torchletNameFromAnnotation);
    }
  }

  private void registerSingletonTorchlet() {
    for (String torchletName : torchLetDefinitions.keySet()) {
      TorchletDefinition torchletDefinition = torchLetDefinitions.get(torchletName);
      if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
        Object component = instantiateTorchlet(torchletDefinition);
        singletonTorchlets.put(torchletName, component);
        logger.atInfo().log("Initialized singleton component: %s\n", torchletName);
      }
    }
  }

  private Object instantiateTorchlet(TorchletDefinition torchletDefinition) {
    Class<?> clazz = torchletDefinition.getClazz();
    try {
      Object instance = clazz.getDeclaredConstructor().newInstance();

      // Getting all fields with @TorchInject annotation.
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(TorchInject.class)) {
          String value = field.getDeclaredAnnotation(TorchInject.class).value();
          Object component =
              getTorchlet(
                  value.isEmpty()
                      ? generateComponentName(field.getType(), Optional.empty())
                      : value);
          field.setAccessible(true);
          field.set(instance, component);
        }
      }
      return instance;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Object getTorchlet(Class<?> aClass) {
    if (!aClass.isAnnotationPresent(Torchlet.class)) {
      throw new RuntimeException(
          String.format("Class %s is not annotated with @Torchlet.", aClass.getName()));
    }
    Torchlet torchletAnnotation = aClass.getDeclaredAnnotation(Torchlet.class);
    String torchletName =
        generateComponentName(
            aClass,
            Optionals.optional(!torchletAnnotation.value().isEmpty(), torchletAnnotation.value()));
    if (verbose) {
      logger.atInfo().log(String.format("Getting Torchlet: %s", torchletName) + "\n");
    }
    return getTorchlet(torchletName);
  }

  public Object getTorchlet(String torchletName) {
    if (torchLetDefinitions.containsKey(torchletName)) {
      if (singletonTorchlets.containsKey(torchletName)) {
        return singletonTorchlets.get(torchletName);
      } else {
        TorchletDefinition torchComponentDefinition = torchLetDefinitions.get(torchletName);
        Object component = instantiateTorchlet(torchComponentDefinition);
        singletonTorchlets.put(torchletName, component);
        return component;
      }
    } else {
      throw new RuntimeException(
          String.format("Torchlet %s is not found in the context.", torchletName));
    }
  }

  private void registerTorchletDefinition(Class<?> aClass, String componentNameFromAnnotation) {
    TorchletDefinition torchComponentDefinition = createTorchletDefinition(aClass);
    this.torchLetDefinitions.put(
        generateComponentName(
            aClass,
            componentNameFromAnnotation.isEmpty()
                ? Optional.empty()
                : Optional.of(componentNameFromAnnotation)),
        torchComponentDefinition);
    logger.atInfo().log(
        "Created torchlet definition: %s\n",
        generateComponentName(
            aClass,
            componentNameFromAnnotation.isEmpty()
                ? Optional.empty()
                : Optional.of(componentNameFromAnnotation)));
  }

  private String packageNameToPath(String packageName) {
    return packageName.replace('.', File.separatorChar);
  }

  private String getToScanPackageName(Class<?> tochHubClass) {
    // Searching for @TorchHub annotation.
    TorchHub torchHubAnnotation = tochHubClass.getAnnotation(TorchHub.class);
    if (torchHubAnnotation == null) {
      throw new RequiredAnnotationNotFoundException(
          String.format(
              "Class %s is not annotated with @TorchHub.", tochHubClass.getCanonicalName()));
    }
    // Get the package name from the torch hub annotation.
    // If empty, use the package name of the torch hub class.
    String packageName = torchHubAnnotation.value();
    if (packageName.isEmpty()) {
      packageName = tochHubClass.getPackage().getName();
    }
    if (verbose) {
      logger.atInfo().log(String.format("Scanning package: %s", packageName) + "\n");
    }
    return packageName;
  }
}
