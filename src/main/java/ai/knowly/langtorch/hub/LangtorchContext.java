package ai.knowly.langtorch.hub;

import static ai.knowly.langtorch.hub.TorchletDefinitionFactory.createTorchletDefinition;
import static ai.knowly.langtorch.hub.TorchletNameGenerator.generateTorchletName;
import static ai.knowly.langtorch.utils.graph.TopologicalSorter.topologicalSort;
import static ai.knowly.langtorch.utils.reflection.ContextUtil.setAccessible;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.LangtorchHubApplication;
import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import ai.knowly.langtorch.hub.exception.ClassInstantiationException;
import ai.knowly.langtorch.hub.exception.MultipleConstructorInjectionException;
import ai.knowly.langtorch.hub.exception.TorchletAlreadyExistsException;
import ai.knowly.langtorch.hub.exception.TorchletDefinitionNotFoundException;
import ai.knowly.langtorch.hub.exception.TorchletInstantiationException;
import ai.knowly.langtorch.hub.schema.Definition;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;
import ai.knowly.langtorch.hub.schema.TorchletDefinition;
import ai.knowly.langtorch.hub.schema.TorchletProviderDefinition;
import ai.knowly.langtorch.hub.schema.TorchletProviderDefinition.TorchletProviderDefinitionBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.flogger.FluentLogger;
import com.google.mu.util.Optionals;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** Torch context contains information about the torchlet and its dependencies. */
public class LangtorchContext {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final boolean verbose;

  private final ConcurrentHashMap<String, Object> singletonTorchlets;
  private final ConcurrentHashMap<String, Definition> componentDefinitions;
  private final ConcurrentHashMap<String, List<String>> dependencyGraph;

  protected LangtorchContext(LangtorchHubConfig langtorchHubConfig) {
    // Deconstruct the config.
    this.verbose = langtorchHubConfig.isVerbose();

    // Initialize the context.
    this.singletonTorchlets = new ConcurrentHashMap<>();
    this.componentDefinitions = new ConcurrentHashMap<>();
    this.dependencyGraph = new ConcurrentHashMap<>();

    if (verbose) {
      logger.atInfo().log("TorchContext Object constructed.\n");
    }
  }

  private static String generateTorchletFromProvider(Method method) {
    return Optionals.optional(
            !method.getDeclaredAnnotation(Provides.class).value().isEmpty(),
            method.getDeclaredAnnotation(Provides.class).value())
        .orElse(method.getReturnType().getName());
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
          registerDefinition(packageName, classLoader, file);
        }
      }
    }
  }

  private void registerDefinition(String packageName, ClassLoader classLoader, File file) {
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
      registerTorchletWithAnnotation(aClass);
      return;
    }
    // Register the class with the TorchletProvider annotation.
    if (aClass.isAnnotationPresent(TorchletProvider.class)) {
      if (verbose) {
        logger.atInfo().log(
            String.format("Found TorchletProvider Class: %s", aClass.getName()) + "\n");
      }
      registerTorchletProviderDefinition(aClass);
    }
  }

  private void registerTorchletWithAnnotation(Class<?> aClass) {
    if (verbose) {
      logger.atInfo().log(String.format("Found Torchlet Class: %s", aClass.getName()) + "\n");
    }
    registerTorchletDefinition(aClass);

    for (Field f : aClass.getDeclaredFields()) {
      if (f.isAnnotationPresent(Inject.class)) {
        if (verbose) {
          logger.atInfo().log(String.format("Found Inject Field: %s", f.getName()) + "\n");
        }
        registerTorchletDefinition(f);
      }
    }
  }

  private void registerTorchletProviderDefinition(Class<?> aClass) {
    for (Method method : aClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Provides.class)) {
        TorchletProviderDefinitionBuilder torchletProviderDefinition =
            TorchletProviderDefinition.builder().setMethod(method).setProviderClass(aClass);
        if (method.isAnnotationPresent(TorchScope.class)) {
          torchletProviderDefinition.setScope(
              method.getDeclaredAnnotation(TorchScope.class).value());
        } else {
          torchletProviderDefinition.setScope(TorchScopeValue.SINGLETON);
        }
        String torchletName = generateTorchletFromProvider(method);
        if (componentDefinitions.containsKey(torchletName)) {
          logger.atWarning().log("Torchlet %s already exists ", torchletName);
          throw new TorchletAlreadyExistsException(
              String.format("Torchlet %s already exists ", torchletName));
        }
        componentDefinitions.put(
            torchletName,
            Definition.builder()
                .setTorchletProviderDefinition(torchletProviderDefinition.build())
                .build());
        dependencyGraph.put(torchletName, new ArrayList<>());
        if (verbose) {
          logger.atInfo().log(
              "Created torchlet definition from provider %s: %s\n", aClass.getName(), torchletName);
          logger.atInfo().log("%s dependencies -> %s\n", torchletName, new ArrayList<>());
        }
      }
    }
  }

  private void registerSingletonTorchlets() {
    topologicalSort(dependencyGraph).stream()
        .map(componentDefinitions::get)
        .forEach(
            definition -> {
              if (definition.getTorchletDefinition().isPresent()) {
                TorchletDefinition torchletDefinition = definition.getTorchletDefinition().get();
                if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
                  Object instance = instantiateTorchlet(torchletDefinition);
                  singletonTorchlets.put(
                      generateTorchletName(torchletDefinition.getClazz()), instance);
                }
                return;
              }

              if (definition.getTorchletProviderDefinition().isPresent()) {
                TorchletProviderDefinition torchletProviderDefinition =
                    definition.getTorchletProviderDefinition().get();
                if (torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
                  Object instance = instantiateTorchletProvider(torchletProviderDefinition);
                  singletonTorchlets.put(
                      generateTorchletFromProvider(torchletProviderDefinition.getMethod()),
                      instance);
                }
              }
            });
  }

  private Object instantiateTorchletProvider(
      TorchletProviderDefinition torchletProviderDefinition) {
    Class<?> providerClass = torchletProviderDefinition.getProviderClass();
    Method method = torchletProviderDefinition.getMethod();

    Object classInstance;
    try {
      classInstance = providerClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      logger.atSevere().log(
          "Error creating instance of class: %s. Class with @TorchletProvider should only have"
              + " no-arg constructor",
          providerClass.getName());
      throw new ClassInstantiationException(e);
    }

    Object methodInstance;
    try {
      methodInstance = method.invoke(classInstance);
    } catch (Exception e) {
      logger.atSevere().log("Error registering bean from @Provides method: %s", method.getName());
      throw new TorchletInstantiationException(e);
    }

    return methodInstance;
  }

  private Object instantiateTorchlet(TorchletDefinition torchletDefinition) {
    Class<?> clazz = torchletDefinition.getClazz();
    ImmutableList<Constructor<?>> injectableConstructors =
        getConstructorWithInjectAnnotation(clazz);

    try {
      Object instance;
      if (injectableConstructors.isEmpty()) {
        // Default constructor
        instance = clazz.getDeclaredConstructor().newInstance();
      } else {
        // Constructor with dependencies
        Constructor<?> injectableConstructor = Iterables.getOnlyElement(injectableConstructors);
        Parameter[] parameters = injectableConstructor.getParameters();
        Object[] paramaterInstances = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
          String dependencyName = generateTorchletName(parameters[i]);
          paramaterInstances[i] = getTorchlet(dependencyName);
        }
        instance = injectableConstructor.newInstance(paramaterInstances);
      }

      // Handle field injection
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Inject.class)) {
          String dependencyName = generateTorchletName(field);
          Object dependencyInstance = getTorchlet(dependencyName);
          // Allows us to set private fields.
          setAccessible(field);
          field.set(instance, dependencyInstance);
        }
      }
      return instance;
    } catch (Exception e) {
      logger.atSevere().log("Error instantiating class %s: %s", clazz.getName(), e.getMessage());
      throw new TorchletInstantiationException(e);
    }
  }

  public Object getTorchlet(Class<?> aClass) {
    // Registered from @Torchlet annotation
    if (aClass.isAnnotationPresent(Torchlet.class)) {
      String torchletName = generateTorchletName(aClass);
      if (verbose) {
        logger.atInfo().log(String.format("Getting Torchlet: %s", torchletName) + "\n");
      }
      return getTorchlet(torchletName);
    }

    // Registered from class with @TorchletProvider and method from @Provides annotation
    return getTorchlet(aClass.getName());
  }

  public Object getTorchlet(String torchletName) {
    if (!componentDefinitions.containsKey(torchletName)) {
      throw new TorchletDefinitionNotFoundException(
          String.format("Torchlet %s is not found in the context.", torchletName));
    }

    Definition definition = componentDefinitions.get(torchletName);

    if (definition.getTorchletDefinition().isPresent()) {
      return processTorchletDefinition(torchletName, definition.getTorchletDefinition().get());
    }

    if (definition.getTorchletProviderDefinition().isPresent()) {
      return processTorchletProviderDefinition(
          torchletName, definition.getTorchletProviderDefinition().get());
    }

    throw new TorchletInstantiationException(
        String.format("Torchlet %s is not found.", torchletName));
  }

  private Object processTorchletProviderDefinition(
      String torchletName, TorchletProviderDefinition torchletProviderDefinition) {
    // If a singleton instance exists, return it.
    if (singletonTorchlets.containsKey(torchletName)
        && torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
      return singletonTorchlets.get(torchletName);
    } else {
      // Else, instantiate a new instance.
      Object component = instantiateTorchletProvider(torchletProviderDefinition);
      // If the scope is SINGLETON, also save this instance for future requests.
      if (torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
        singletonTorchlets.put(torchletName, component);
      }
      return component;
    }
  }

  private Object processTorchletDefinition(
      String torchletName, TorchletDefinition torchletDefinition) {
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
    String torchletName = generateTorchletName(aClass);
    this.componentDefinitions.put(
        torchletName,
        Definition.builder().setTorchletDefinition(createTorchletDefinition(aClass)).build());
    if (verbose) {
      logger.atInfo().log("Created torchlet definition: %s\n", torchletName);
    }

    updateDependencyGraph(aClass);
  }

  private void registerTorchletDefinition(Field field) {
    String torchletName = generateTorchletName(field);
    this.componentDefinitions.put(
        torchletName,
        Definition.builder().setTorchletDefinition(createTorchletDefinition(field)).build());
    if (verbose) {
      logger.atInfo().log("Created torchlet definition: %s\n", torchletName);
    }

    this.dependencyGraph.put(torchletName, new ArrayList<>());
  }

  private void updateDependencyGraph(Class<?> aClass) {
    String torchletName = generateTorchletName(aClass);
    ImmutableList<Constructor<?>> constructors = getConstructorWithInjectAnnotation(aClass);

    List<String> dependencies = new ArrayList<>();
    if (!constructors.isEmpty()) {
      // Getting all dependencies from the constructor.
      Parameter[] parameters = Iterables.getOnlyElement(constructors).getParameters();
      for (Parameter parameter : parameters) {
        String tn = generateTorchletName(parameter);
        dependencies.add(tn);
      }
    }
    dependencyGraph.put(torchletName, dependencies);
    if (verbose) {
      logger.atInfo().log("%s dependencies -> %s\n", torchletName, dependencies);
    }
  }

  private ImmutableList<Constructor<?>> getConstructorWithInjectAnnotation(Class<?> aClass) {
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
    // Searching for @LangtorchHubApplication annotation.
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
