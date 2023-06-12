package ai.knowly.langtorch.hub;

import static ai.knowly.langtorch.hub.TorchletDefinitionFactory.createTorchletDefinition;
import static ai.knowly.langtorch.hub.TorchletDefinitionRegistry.getConstructorWithInjectAnnotation;
import static ai.knowly.langtorch.hub.TorchletNameGenerator.generateTorchletName;
import static ai.knowly.langtorch.utils.reflection.ContextUtil.setAccessible;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Named;
import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.exception.ClassInstantiationException;
import ai.knowly.langtorch.hub.exception.TorchletAlreadyExistsException;
import ai.knowly.langtorch.hub.exception.TorchletDefinitionNotFoundException;
import ai.knowly.langtorch.hub.exception.TorchletInstantiationException;
import ai.knowly.langtorch.hub.schema.Definition;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;
import ai.knowly.langtorch.hub.schema.TorchletDefinition;
import ai.knowly.langtorch.hub.schema.TorchletProviderDefinition;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.flogger.FluentLogger;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/** Torch context contains information about the torchlet and its dependencies. */
public class LangtorchContext {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final boolean verbose;
  private final TorchletSingletonRegistry torchletSingletonRegistry =
      TorchletSingletonRegistry.create();
  private final TorchletDefinitionRegistry torchletDefinitionRegistry =
      TorchletDefinitionRegistry.create();
  private final TorchletScanner torchletScanner;

  protected LangtorchContext(LangtorchHubConfig langtorchHubConfig) {
    this.verbose = langtorchHubConfig.isVerbose();
    this.torchletScanner = new TorchletScanner(this.getClass().getClassLoader());
    logVerboseInfo("TorchContext Object constructed.");
  }

  public void init(Class<?> tochHubClass) {
    torchletScanner.scan(tochHubClass).forEach(this::registerDefinition);
    instantiateSingletonTorchlets();
  }

  private void registerDefinition(Class<?> aClass) {
    if (aClass.isAnnotationPresent(Torchlet.class)) {
      registerTorchletClassDefinition(aClass);
    } else if (aClass.isAnnotationPresent(TorchletProvider.class)) {
      registerTorchletProviderDefinition(aClass);
    }
  }

  private TorchScopeValue getTorchScopeValue(AnnotatedElement element) {
    if (element.isAnnotationPresent(TorchScope.class)) {
      return element.getDeclaredAnnotation(TorchScope.class).value();
    }
    return TorchScopeValue.SINGLETON;
  }

  private void registerTorchletProviderDefinition(Class<?> aClass) {
    Method[] declaredMethods = aClass.getDeclaredMethods();
    ImmutableList.copyOf(declaredMethods).stream()
        .filter(m -> m.isAnnotationPresent(Provides.class))
        .forEach(
            method -> {
              TorchletProviderDefinition torchletProviderDefinition =
                  TorchletProviderDefinition.builder()
                      .setMethod(method)
                      .setProviderClass(aClass)
                      .setScope(getTorchScopeValue(method))
                      .build();

              // Check if the torchlet already exists
              if (torchletDefinitionRegistry.containsTorchletDefinition(method)) {
                String torchletName = generateTorchletName(method);
                logger.atWarning().log("Torchlet %s already exists ", torchletName);
                throw new TorchletAlreadyExistsException(
                    String.format("Torchlet %s already exists ", torchletName));
              }
              torchletDefinitionRegistry.addTorchletDefinition(
                  method,
                  Definition.builder()
                      .setTorchletProviderDefinition(torchletProviderDefinition)
                      .build());
              torchletDefinitionRegistry.addDependency(method, new ArrayList<>());
            });
  }

  private void instantiateSingletonTorchlets() {
    torchletDefinitionRegistry
        .getTopologicallySortedDefinition()
        .forEach(this::instantiateFromDefinition);
  }

  private Object instantiateFromDefinition(Definition definition) {
    // Class with @Torchlet annotation
    if (definition.getTorchletDefinition().isPresent()) {
      TorchletDefinition torchletDefinition = definition.getTorchletDefinition().get();
      if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
        Object instance = instantiateTorchletClass(torchletDefinition);
        torchletSingletonRegistry.addTorchletSingletonWithTorchletAnnotation(
            torchletDefinition.getClazz(), instance);
        return instance;
      } else {
        return instantiateTorchletClass(torchletDefinition);
      }
    }
    // Class with @TorchletProvider annotation
    if (definition.getTorchletProviderDefinition().isPresent()) {
      TorchletProviderDefinition torchletProviderDefinition =
          definition.getTorchletProviderDefinition().get();
      if (torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
        Object instance = instantiateTorchletProvider(torchletProviderDefinition);
        torchletSingletonRegistry.addTorchletSingletonWithTorchletProviderAnnotation(
            torchletProviderDefinition.getMethod(), instance);
        return instance;
      } else {
        return instantiateTorchletProvider(torchletProviderDefinition);
      }
    }
    throw new TorchletInstantiationException("Cannot instantiate from definition: " + definition);
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

  private Object instantiateTorchletClass(TorchletDefinition torchletDefinition) {
    Class<?> clazz = torchletDefinition.getClazz();
    ImmutableList<Constructor<?>> injectableConstructors =
        getConstructorWithInjectAnnotation(clazz);
    try {
      Object instance = createInstance(clazz, injectableConstructors);
      injectFields(instance, clazz);
      return instance;
    } catch (Exception e) {
      logger.atSevere().log("Error instantiating class %s:%s", clazz.getName(), e.getMessage());
      throw new TorchletInstantiationException(e);
    }
  }

  private Object createInstance(
      Class<?> clazz, ImmutableList<Constructor<?>> injectableConstructors)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {
    if (injectableConstructors.isEmpty()) {
      return clazz.getDeclaredConstructor().newInstance();
    } else {
      Constructor<?> injectableConstructor = Iterables.getOnlyElement(injectableConstructors);
      Parameter[] parameters = injectableConstructor.getParameters();
      Object[] parameterInstances = createParameterInstances(parameters, clazz);
      return injectableConstructor.newInstance(parameterInstances);
    }
  }

  private Object[] createParameterInstances(Parameter[] parameters, Class<?> clazz)
      throws TorchletInstantiationException {
    Object[] parameterInstances = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      parameterInstances[i] = getParameterInstance(parameter, clazz);
    }
    return parameterInstances;
  }

  private Object getParameterInstance(Parameter parameter, Class<?> clazz) {
    Optional<String> namedAnnotationValue = getNamedAnnotationValue(parameter);
    if (namedAnnotationValue.isPresent() && !namedAnnotationValue.get().isEmpty()) {
      return getTorchletInstanceByName(namedAnnotationValue.get(), clazz);
    } else {
      return getTorchletInstanceByType(parameter, clazz);
    }
  }

  private Object getTorchletInstanceByName(String dependencyName, Class<?> clazz) {
    Optional<Definition> definition =
        torchletDefinitionRegistry.getTorchletDefinition(dependencyName);
    if (!definition.isPresent()) {
      logger.atSevere().log(
          "Error instantiating class %s:Torchlet %s does not exist",
          clazz.getName(), dependencyName);
      throw new TorchletInstantiationException(
          String.format(
              "Error instantiating class %s:Torchlet %s does not exist",
              clazz.getName(), dependencyName));
    }
    return torchletSingletonRegistry
        .getTorchletSingletonByName(dependencyName)
        .orElseGet(() -> instantiateFromDefinition(definition.get()));
  }

  private Object getTorchletInstanceByType(Parameter parameter, Class<?> clazz) {
    Optional<Definition> definition = torchletDefinitionRegistry.getTorchletDefinition(parameter);
    if (!definition.isPresent()) {
      throw new TorchletInstantiationException(
          String.format("Error instantiating class %s", clazz.getName()));
    }
    Set<Object> torchletSingletonByType =
        torchletSingletonRegistry.getTorchletSingletonByType(parameter.getType());
    if (torchletSingletonByType.size() > 1) {
      throw new TorchletInstantiationException(
          String.format(
              "Error instantiating class %s:More than one instance of %s found",
              clazz.getName(), parameter.getType().getName()));
    }
    if (torchletSingletonByType.size() == 1) {
      return Iterables.getOnlyElement(torchletSingletonByType);
    }
    return instantiateFromDefinition(definition.get());
  }

  private void injectFields(Object instance, Class<?> clazz) throws IllegalAccessException {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Inject.class)) {
        if (field.isAnnotationPresent(Named.class)
            && !field.getAnnotation(Named.class).value().isEmpty()) {
          String dependencyName = field.getAnnotation(Named.class).value();
          Optional<Object> torchletSingletonByName =
              torchletSingletonRegistry.getTorchletSingletonByName(dependencyName);
          if (torchletSingletonByName.isPresent()) {
            setAccessible(field);
            field.set(instance, torchletSingletonByName.get());
          }
        } else {
          Object dependencyInstance = getTorchletByType(field.getType());
          setAccessible(field);
          field.set(instance, dependencyInstance);
        }
      }
    }
  }

  public Optional<String> getNamedAnnotationValue(AnnotatedElement element) {
    if (element.isAnnotationPresent(Named.class)) {
      return Optional.of(element.getAnnotation(Named.class).value());
    }
    return Optional.empty();
  }

  public Object getTorchletByType(Class<?> aClass) {
    Set<Object> torchletSingletonByType =
        torchletSingletonRegistry.getTorchletSingletonByType(aClass);

    if (torchletSingletonByType.size() > 1) {
      throw new TorchletInstantiationException(
          String.format(
              "Error instantiating class %s: More than one instance of %s found",
              aClass.getName(), aClass.getName()));
    }
    if (torchletSingletonByType.size() == 1) {
      return Iterables.getOnlyElement(torchletSingletonByType);
    }
    if (!torchletDefinitionRegistry.containsTorchletDefinition(aClass)) {
      throw new TorchletDefinitionNotFoundException(
          String.format("Torchlet %s is not found in the context.", aClass.getName()));
    }

    return torchletDefinitionRegistry
        .getTorchletDefinition(aClass)
        .map(
            def -> {
              if (def.getTorchletDefinition().isPresent()) {
                return processTorchletDefinitionByClass(aClass, def.getTorchletDefinition().get());
              }
              if (def.getTorchletProviderDefinition().isPresent()) {
                return processTorchletProviderDefinitionByClass(
                    aClass, def.getTorchletProviderDefinition().get());
              }
              return null;
            })
        .orElseThrow(
            () ->
                new TorchletInstantiationException(
                    String.format("Torchlet %s is not found.", aClass.getName())));
  }

  public Object getTorchletByName(String torchletName) {
    if (!torchletDefinitionRegistry.containsTorchletDefinition(torchletName)) {
      throw new TorchletDefinitionNotFoundException(
          String.format("Torchlet %s is not found in the context.", torchletName));
    }

    return torchletDefinitionRegistry
        .getTorchletDefinition(torchletName)
        .map(
            def -> {
              if (def.getTorchletDefinition().isPresent()) {
                return processTorchletDefinitionByName(
                    torchletName, def.getTorchletDefinition().get());
              }
              if (def.getTorchletProviderDefinition().isPresent()) {
                return processTorchletProviderDefinitionByName(
                    torchletName, def.getTorchletProviderDefinition().get());
              }
              return null;
            })
        .orElseThrow(
            () ->
                new TorchletInstantiationException(
                    String.format("Torchlet %s is not found.", torchletName)));
  }

  private void logVerboseInfo(String message, Object... args) {
    if (verbose) {
      logger.atInfo().log(message, args);
    }
  }

  private Object processTorchletProviderDefinitionByName(
      String torchletName, TorchletProviderDefinition torchletProviderDefinition) {
    // If a singleton instance exists, return it.
    Optional<Object> torchletSingletonByName =
        torchletSingletonRegistry.getTorchletSingletonByName(torchletName);
    if (torchletSingletonByName.isPresent()
        && torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
      return torchletSingletonByName.get();
    } else {
      // Else, instantiate a new instance.
      Object component = instantiateTorchletProvider(torchletProviderDefinition);
      // If the scope is SINGLETON, also save this instance for future requests.
      if (torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
        torchletSingletonRegistry.addTorchletSingletonByName(torchletName, component);
      }
      return component;
    }
  }

  private Object processTorchletProviderDefinitionByClass(
      Class<?> clazz, TorchletProviderDefinition torchletProviderDefinition) {
    // If a singleton instance exists, return it.
    Set<Object> torchletSingletonByClass =
        torchletSingletonRegistry.getTorchletSingletonByType(clazz);
    if (!torchletSingletonByClass.isEmpty()) {
      if (torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {

        if (torchletSingletonByClass.size() > 1) {
          throw new TorchletInstantiationException(
              String.format(
                  "Multiple singleton instances of class %s found in the context.",
                  clazz.getName()));
        }
        return Iterables.getOnlyElement(torchletSingletonByClass);
      }

    } else {
      // Else, instantiate a new instance.
      Object component = instantiateTorchletProvider(torchletProviderDefinition);
      // If the scope is SINGLETON, also save this instance for future requests.
      if (torchletProviderDefinition.getScope() == TorchScopeValue.SINGLETON) {
        torchletSingletonRegistry.addTorchletSingletonByClass(clazz, component);
      }
      return component;
    }
    return null;
  }

  private Object processTorchletDefinitionByName(
      String torchletName, TorchletDefinition torchletDefinition) {
    // If a singleton instance exists, return it.
    Optional<Object> torchletSingletonByName =
        torchletSingletonRegistry.getTorchletSingletonByName(torchletName);
    if (torchletSingletonByName.isPresent()
        && torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
      return torchletSingletonByName.get();
    } else {
      // Else, instantiate a new instance.
      Object component = instantiateTorchletClass(torchletDefinition);
      // If the scope is SINGLETON, also save this instance for future requests.
      if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
        torchletSingletonRegistry.addTorchletSingletonByName(torchletName, component);
      }
      return component;
    }
  }

  private Object processTorchletDefinitionByClass(
      Class<?> clazz, TorchletDefinition torchletDefinition) {
    // If a singleton instance exists, return it.
    Set<Object> torchletSingletonByClass =
        torchletSingletonRegistry.getTorchletSingletonByType(clazz);

    if (!torchletSingletonByClass.isEmpty()) {
      if (torchletSingletonByClass.size() > 1) {
        throw new TorchletInstantiationException(
            String.format(
                "Multiple instances of %s found in the context. "
                    + "Please use @Named annotation to specify the instance.",
                clazz.getName()));
      }
      if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
        return Iterables.getOnlyElement(torchletSingletonByClass);
      }
    } else {
      // Else, instantiate a new instance.
      Object component = instantiateTorchletClass(torchletDefinition);
      // If the scope is SINGLETON, also save this instance for future requests.
      if (torchletDefinition.getScope() == TorchScopeValue.SINGLETON) {
        torchletSingletonRegistry.addTorchletSingletonByClass(clazz, component);
      }
      return component;
    }
    return null;
  }

  private void registerTorchletClassDefinition(Class<?> aClass) {
    torchletDefinitionRegistry.addTorchletDefinition(
        aClass,
        Definition.builder().setTorchletDefinition(createTorchletDefinition(aClass)).build());
    torchletDefinitionRegistry.updateDependencyForConstructor(aClass);
  }
}
