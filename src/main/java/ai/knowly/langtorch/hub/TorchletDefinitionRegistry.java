package ai.knowly.langtorch.hub;

import static ai.knowly.langtorch.hub.TorchletNameGenerator.generateTorchletName;
import static ai.knowly.langtorch.utils.graph.TopologicalSorter.topologicalSort;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.exception.MultipleConstructorInjectionException;
import ai.knowly.langtorch.hub.schema.Definition;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.flogger.FluentLogger;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TorchletDefinitionRegistry {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ConcurrentHashMap<String, Definition> torchletDefinitions =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, List<String>> dependencyGraph = new ConcurrentHashMap<>();

  public static TorchletDefinitionRegistry create() {
    return new TorchletDefinitionRegistry();
  }

  public static ImmutableList<Constructor<?>> getConstructorWithInjectAnnotation(Class<?> aClass) {
    // Getting all constructors with @Inject annotation.
    ImmutableList<Constructor<?>> constructors =
        Arrays.stream(aClass.getConstructors())
            .filter(c -> c.isAnnotationPresent(Inject.class))
            .collect(toImmutableList());

    if (constructors.size() > 1) {
      throw new MultipleConstructorInjectionException(
          "Multiple constructors with @Inject annotation.");
    }
    return constructors;
  }

  public void addTorchletDefinition(AnnotatedElement element, Definition definition) {
    String torchletName = generateTorchletName(element);
    logger.atInfo().log("Adding definition for %s", torchletName);
    this.torchletDefinitions.put(torchletName, definition);
  }

  public boolean containsTorchletDefinition(AnnotatedElement element) {
    return this.torchletDefinitions.containsKey(generateTorchletName(element));
  }

  public boolean containsTorchletDefinition(String torchletName) {
    return this.torchletDefinitions.containsKey(torchletName);
  }

  public Optional<Definition> getTorchletDefinition(AnnotatedElement element) {
    return getTorchletDefinition(generateTorchletName(element));
  }

  public Optional<Definition> getTorchletDefinition(String torchletName) {
    return Optional.ofNullable(this.torchletDefinitions.get(torchletName));
  }

  public void addDependency(String torchletName, @NonNull List<String> dependencies) {
    logger.atInfo().log("Adding dependencies for %s -> %s", torchletName, dependencies);
    this.dependencyGraph.putIfAbsent(torchletName, new ArrayList<>());
    this.dependencyGraph.get(torchletName).addAll(dependencies);
  }

  public void addDependency(AnnotatedElement element, @NonNull List<String> dependencies) {
    String torchletName = generateTorchletName(element);
    addDependency(torchletName, dependencies);
  }

  public ImmutableList<Definition> getTopologicallySortedDefinition() {
    return topologicalSort(this.dependencyGraph).stream()
        .map(torchletDefinitions::get)
        .collect(ImmutableList.toImmutableList());
  }

  public void updateDependencyForConstructor(Class<?> aClass) {
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
    addDependency(torchletName, dependencies);
  }
}
