package ai.knowly.langtorch.hub;

import static ai.knowly.langtorch.utils.graph.TopologicalSorter.topologicalSort;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.LangtorchHubApplication;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import ai.knowly.langtorch.hub.exception.MultipleConstructorInjectionException;
import ai.knowly.langtorch.hub.exception.TorchletDefinitionNotFoundException;
import ai.knowly.langtorch.hub.exception.TorchletInstantiationException;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;
import ai.knowly.langtorch.hub.schema.TorchletDefinition;
import ai.knowly.langtorch.hub.schema.TorchletDefinition.TorchletDefinitionBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.flogger.FluentLogger;
import com.google.mu.util.Optionals;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/** Torch context contains information about the torchlet and its dependencies. */
public class LangtorchContext {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final boolean verbose;

  private final ConcurrentHashMap<String, Object> singletonTorchlets;
  private final ConcurrentHashMap<String, TorchletDefinition> torchletDefinitions;
  private final ConcurrentHashMap<String, List<String>> dependencyGraph;

  public LangtorchContext(LangtorchHubConfig langtorchHubConfig) {
    // Deconstruct the config.
    this.verbose = langtorchHubConfig.isVerbose();

    // Initialize the context.
    this.singletonTorchlets = new ConcurrentHashMap<>();
    this.torchletDefinitions = new ConcurrentHashMap<>();
    this.dependencyGraph = new ConcurrentHashMap<>();

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

  private static String generateTorchletName(Class<?> aClass) {
    return Optionals.optional(
            !aClass.getDeclaredAnnotation(Torchlet.class).value().isEmpty(),
            aClass.getDeclaredAnnotation(Torchlet.class).value())
        .orElse(aClass.getName());
  }

  public void init(Class<?> tochHubClass) {
    // Get the package name to scan.
    String toScanPackageName = getToScanPackageName(tochHubClass);
    // Scan the package and register the torchlet definitions.
    scanPackageAndRegisterTorchletDefinitions(toScanPackageName);
    // Create the singleton torchlets from the definitions.
    registerSingletonTorchlets();
  }

  private void scanPackageAndRegisterTorchletDefinitions(String packageName) {
    ClassLoader classLoader = LangtorchContext.class.getClassLoader();
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
      logger.atWarning().log("Class not found: %s.%s and fail to load.", packageName, className);
      return;
    }
    // Only register the class with the Torchlet annotation.
    if (aClass.isAnnotationPresent(Torchlet.class)) {
      if (verbose) {
        logger.atInfo().log(String.format("Found Torchlet Class: %s", aClass.getName()) + "\n");
      }
      registerTorchletDefinition(aClass);
    }
  }

  private void registerSingletonTorchlets() {
    List<String> strings = topologicalSort(dependencyGraph);
    strings.stream()
        .map(torchletDefinitions::get)
        .forEach(
            torchletDefinition -> {
              if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
                Object instance = instantiateTorchlet(torchletDefinition);
                singletonTorchlets.put(
                    generateTorchletName(torchletDefinition.getClazz()), instance);
              }
            });
  }

  private Object instantiateTorchlet(TorchletDefinition torchletDefinition) {
    Class<?> clazz = torchletDefinition.getClazz();
    ImmutableList<Constructor<?>> injectableConstructors = getInjectableConstructor(clazz);

    try {
      if (injectableConstructors.isEmpty()) {
        // Default constructor
        return clazz.getDeclaredConstructor().newInstance();
      } else {
        // Constructor with dependencies
        Constructor<?> injectableConstructor = Iterables.getOnlyElement(injectableConstructors);
        Class<?>[] parameterTypes = injectableConstructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
          String dependencyName = generateTorchletName(parameterTypes[i]);
          parameters[i] = getTorchlet(dependencyName);
        }
        return injectableConstructor.newInstance(parameters);
      }

    } catch (Exception e) {
      logger.atSevere().log("Error instantiating class %s: %s", clazz.getName(), e.getMessage());
      throw new TorchletInstantiationException(e);
    }
  }

  public Object getTorchlet(Class<?> aClass) {
    if (!aClass.isAnnotationPresent(Torchlet.class)) {
      throw new AnnotationNotFoundException(
          String.format("Class %s is not annotated with @Torchlet.", aClass.getName()));
    }

    String torchletName = generateTorchletName(aClass);
    if (verbose) {
      logger.atInfo().log(String.format("Getting Torchlet: %s", torchletName) + "\n");
    }
    return getTorchlet(torchletName);
  }

  public Object getTorchlet(String torchletName) {
    if (!torchletDefinitions.containsKey(torchletName)) {
      throw new TorchletDefinitionNotFoundException(
          String.format("Torchlet %s is not found in the context.", torchletName));
    }

    TorchletDefinition torchletDefinition = torchletDefinitions.get(torchletName);

    // If a singleton instance exists, return it.
    if (singletonTorchlets.containsKey(torchletName)
        && torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
      return singletonTorchlets.get(torchletName);
    } else {
      // Else, instantiate a new instance.
      Object component = instantiateTorchlet(torchletDefinition);
      // If the scope is SINGLETON, also save this instance for future requests.
      if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
        singletonTorchlets.put(torchletName, component);
      }
      return component;
    }
  }

  private void registerTorchletDefinition(Class<?> aClass) {
    TorchletDefinition torchComponentDefinition = createTorchletDefinition(aClass);
    String torchletName = generateTorchletName(aClass);
    this.torchletDefinitions.put(torchletName, torchComponentDefinition);
    if (verbose) {
      logger.atInfo().log("Created torchlet definition: %s\n", torchletName);
    }

    updateDependencyGraph(aClass);
  }

  private void updateDependencyGraph(Class<?> aClass) {
    String torchletName = generateTorchletName(aClass);
    ImmutableList<Constructor<?>> constructors = getInjectableConstructor(aClass);

    List<String> dependencies = new ArrayList<>();
    if (!constructors.isEmpty()) {
      // Getting all dependencies from the constructor.
      dependencies =
          Arrays.stream(Iterables.getOnlyElement(constructors).getParameterTypes())
              .map(LangtorchContext::generateTorchletName)
              .collect(Collectors.toList());
    }
    dependencyGraph.put(torchletName, dependencies);
    if (verbose) {
      logger.atInfo().log("%s dependencies -> %s\n", torchletName, dependencies);
    }
  }

  private ImmutableList<Constructor<?>> getInjectableConstructor(Class<?> aClass) {
    // Getting all constructors with @TorchInject annotation.
    ImmutableList<Constructor<?>> constructors =
        Arrays.stream(aClass.getConstructors())
            .filter(c -> c.isAnnotationPresent(Inject.class))
            .collect(toImmutableList());

    if (constructors.size() > 1) {
      throw new MultipleConstructorInjectionException(
          "Multiple constructors with @TorchInject annotation.");
    }
    return constructors;
  }

  private String packageNameToPath(String packageName) {
    return packageName.replace('.', File.separatorChar);
  }

  private String getToScanPackageName(Class<?> tochHubClass) {
    // Searching for @TorchHub annotation.
    LangtorchHubApplication torchHubAnnotation =
        tochHubClass.getAnnotation(LangtorchHubApplication.class);
    if (torchHubAnnotation == null) {
      throw new AnnotationNotFoundException(
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
