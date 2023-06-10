package ai.knowly.langtorch.hub;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TorchletFactory {

  private final ConcurrentHashMap<Class<?>, Set<Object>> torchletDefinitions =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<Class<?>, Set<Object>> singletonTorchlets =
      new ConcurrentHashMap<>();

  public static TorchletFactory create() {
    return new TorchletFactory();
  }

  // Helper method to get all classes and interfaces a class is assignable from.
  private Set<Class<?>> getAllClassesAndInterfaces(Class<?> aClass) {
    Set<Class<?>> classes = new HashSet<>();
    while (aClass != null) {
      classes.add(aClass);
      classes.addAll(Arrays.asList(aClass.getInterfaces()));
      aClass = aClass.getSuperclass();
    }
    return classes;
  }

  public void addTorchletDefinition(Class<?> clazz, Object torchletDefinition) {
    getAllClassesAndInterfaces(clazz)
        .forEach(
            c ->
                torchletDefinitions
                    .computeIfAbsent(c, k -> new HashSet<>())
                    .add(torchletDefinition));
  }

  public void addSingletonTorchlet(Class<?> clazz, Object torchlet) {
    getAllClassesAndInterfaces(clazz)
        .forEach(c -> singletonTorchlets.computeIfAbsent(c, k -> new HashSet<>()).add(torchlet));
  }

  public Set<Object> getTorchletDefinitions(Class<?> clazz) {
    return torchletDefinitions.getOrDefault(clazz, new HashSet<>());
  }

  public Set<Object> getSingletonTorchlets(Class<?> clazz) {
    return singletonTorchlets.getOrDefault(clazz, new HashSet<>());
  }
}
