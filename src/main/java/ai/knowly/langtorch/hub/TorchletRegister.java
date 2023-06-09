package ai.knowly.langtorch.hub;

import static ai.knowly.langtorch.hub.TorchletDefinitionFactory.createTorchletDefinition;
import static ai.knowly.langtorch.hub.TorchletNameGenerator.generateTorchletName;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.exception.MultipleConstructorInjectionException;
import ai.knowly.langtorch.hub.exception.TorchletAlreadyExistsException;
import ai.knowly.langtorch.hub.schema.Definition;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;
import ai.knowly.langtorch.hub.schema.TorchletProviderDefinition;
import ai.knowly.langtorch.hub.schema.TorchletProviderDefinition.TorchletProviderDefinitionBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.flogger.FluentLogger;
import com.google.mu.util.Optionals;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TorchletRegister {
  private final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final boolean verbose;
  private final TorchletScanner torchletScanner;
  private final ConcurrentHashMap<String, Definition> componentDefinitions =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, List<String>> dependencyGraph = new ConcurrentHashMap<>();

  public TorchletRegister(boolean verbose, ClassLoader classLoader) {
    this.verbose = verbose;
    this.torchletScanner = new TorchletScanner(classLoader);
    logVerboseInfo("TorchletRegister Object constructed.");
  }

  private static String generateTorchletFromProvider(Method method) {
    return Optionals.optional(
            !method.getDeclaredAnnotation(Provides.class).value().isEmpty(),
            method.getDeclaredAnnotation(Provides.class).value())
        .orElse(method.getReturnType().getName());
  }

  public void register(Class<?> torchHubClass) {
    torchletScanner.scan(torchHubClass).forEach(this::registerDefinition);
  }

  private void registerDefinition(Class<?> aClass) {
    if (aClass.isAnnotationPresent(Torchlet.class)) {
      registerTorchletWithAnnotation(aClass);
    } else if (aClass.isAnnotationPresent(TorchletProvider.class)) {
      logVerboseInfo("Found TorchletProvider Class:%s", aClass.getName());
      registerTorchletProviderDefinition(aClass);
    }
  }

  private void registerTorchletWithAnnotation(Class<?> aClass) {
    logVerboseInfo("Found Torchlet Class: %s", aClass.getName());
    registerTorchletDefinition(aClass);
    for (Field field : aClass.getDeclaredFields()) {
      if (field.isAnnotationPresent(Inject.class)) {
        logVerboseInfo("Found Inject Field: %s", field.getName());
        registerFieldDefinition(field);
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

        logVerboseInfo(
            "Created torchlet definition from provider %s: %s\n", aClass.getName(), torchletName);
        logVerboseInfo("%s dependencies -> %s\n", torchletName, new ArrayList<>());
      }
    }
  }

  private void registerTorchletDefinition(Class<?> aClass) {
    String torchletName = generateTorchletName(aClass);
    this.componentDefinitions.put(
        torchletName,
        Definition.builder().setTorchletDefinition(createTorchletDefinition(aClass)).build());

    logVerboseInfo("Created torchlet definition: %s\n", torchletName);

    updateDependencyGraph(aClass);
  }

  private void logVerboseInfo(String message, Object... args) {
    if (verbose) {
      logger.atInfo().log(message, args);
    }
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

    logVerboseInfo("%s dependencies -> %s\n", torchletName, dependencies);
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

  private void registerFieldDefinition(Field field) {
    String torchletName = generateTorchletName(field);
    this.componentDefinitions.put(
        torchletName,
        Definition.builder().setTorchletDefinition(createTorchletDefinition(field)).build());
    logVerboseInfo("Created torchlet definition: %s", torchletName);
    this.dependencyGraph.put(torchletName, new ArrayList<>());
  }
}
