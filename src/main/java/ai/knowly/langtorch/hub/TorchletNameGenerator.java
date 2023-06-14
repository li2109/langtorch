package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.annotation.Named;
import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import com.google.mu.util.Optionals;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.AllArgsConstructor;

/** Generates a name for a Torchlet. */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TorchletNameGenerator {
  public static String generateTorchletName(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return generateTorchletName((Class<?>) element);
    } else if (element instanceof Field) {
      return generateTorchletName((Field) element);
    } else if (element instanceof Parameter) {
      return generateTorchletName((Parameter) element);
    } else if (element instanceof Method) {
      return generateTorchletFromProvider((Method) element);
    } else {
      throw new IllegalArgumentException(
          "Unsupported AnnotatedElement type: " + element.getClass());
    }
  }

  private static String generateTorchletName(Class<?> aClass) {
    boolean hasTorchletAnnotationValue =
        aClass.isAnnotationPresent(Torchlet.class)
            && !aClass.getDeclaredAnnotation(Torchlet.class).value().isEmpty();
    if (hasTorchletAnnotationValue) {
      return aClass.getDeclaredAnnotation(Torchlet.class).value();
    }
    return aClass.getName();
  }

  private static String generateTorchletName(Parameter parameter) {
    boolean hasNamedAnnotationValue =
        parameter.isAnnotationPresent(Named.class)
            && !parameter.getDeclaredAnnotation(Named.class).value().isEmpty();
    if (hasNamedAnnotationValue) {
      return parameter.getDeclaredAnnotation(Named.class).value();
    }
    return parameter.getType().getName();
  }

  private static String generateTorchletName(Field field) {
    boolean hasNamedAnnotationValue =
        field.isAnnotationPresent(Named.class)
            && !field.getDeclaredAnnotation(Named.class).value().isEmpty();
    if (hasNamedAnnotationValue) {
      return field.getDeclaredAnnotation(Named.class).value();
    }
    return field.getType().getName();
  }

  private static String generateTorchletFromProvider(Method method) {
    return Optionals.optional(
            !method.getDeclaredAnnotation(Provides.class).value().isEmpty(),
            method.getDeclaredAnnotation(Provides.class).value())
        .orElse(method.getReturnType().getName());
  }
}
