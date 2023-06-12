package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TorchletSingletonRegistry {
  private final ConcurrentHashMap<Class<?>, Set<Object>> singletonTorchletByClass =
      new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Object> singletonTorchletByName =
      new ConcurrentHashMap<>();

  public static TorchletSingletonRegistry create() {
    return new TorchletSingletonRegistry();
  }

  private Set<Class<?>> getAllClassesAndInterfaces(Class<?> aClass) {
    Set<Class<?>> classes = new HashSet<>();
    while (aClass != null) {
      classes.add(aClass);
      classes.addAll(Arrays.asList(aClass.getInterfaces()));
      aClass = aClass.getSuperclass();
    }
    return classes;
  }

  private void addTorchletRelatedClasses(Class<?> clazz, Object singleton) {
    getAllClassesAndInterfaces(clazz).stream()
        .filter(c -> !c.equals(Object.class))
        .forEach(
            c -> {
              singletonTorchletByClass.computeIfAbsent(c, k -> new HashSet<>());
              singletonTorchletByClass.get(c).add(singleton);
            });
  }

  public void addTorchletSingletonWithTorchletAnnotation(Class<?> clazz, Object singleton) {
    String annotationValue = clazz.getAnnotation(Torchlet.class).value();
    if (annotationValue.isEmpty()) {
      addTorchletRelatedClasses(clazz, singleton);
    } else {
      addTorchletSingletonByName(annotationValue, singleton);
    }
  }

  public void addTorchletSingletonByName(String name, Object instance) {
    singletonTorchletByName.put(name, instance);
  }

  public void addTorchletSingletonByClass(Class<?> clazz, Object instance) {
    addTorchletRelatedClasses(clazz, instance);
  }

  public void addTorchletSingletonWithTorchletProviderAnnotation(Method method, Object singleton) {
    String annotationValue = method.getAnnotation(Provides.class).value();
    if (annotationValue.isEmpty()) {
      addTorchletRelatedClasses(method.getReturnType(), singleton);
    } else {
      singletonTorchletByName.put(annotationValue, singleton);
    }
  }

  public Set<Object> getTorchletSingletonByType(Class<?> clazz) {
    return singletonTorchletByClass.getOrDefault(clazz, new HashSet<>());
  }

  public Optional<Object> getTorchletSingletonByName(String name) {
    return Optional.ofNullable(singletonTorchletByName.get(name));
  }
}
