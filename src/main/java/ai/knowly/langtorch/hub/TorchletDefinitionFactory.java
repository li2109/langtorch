package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;
import ai.knowly.langtorch.hub.schema.TorchletDefinition;
import ai.knowly.langtorch.hub.schema.TorchletDefinition.TorchletDefinitionBuilder;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import lombok.AllArgsConstructor;

/** Factory for creating TorchletDefinition from AnnotatedElement. */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TorchletDefinitionFactory {
  public static TorchletDefinition createTorchletDefinition(AnnotatedElement element) {
    TorchletDefinitionBuilder torchletDef = TorchletDefinition.builder();
    torchletDef.setClazz(getElementType(element));
    if (element.isAnnotationPresent(TorchScope.class)) {
      TorchScopeValue value = element.getDeclaredAnnotation(TorchScope.class).value();
      torchletDef.setScope(value);
    } else {
      torchletDef.setScope(TorchScopeValue.SINGLETON);
    }
    return torchletDef.build();
  }

  private static Class<?> getElementType(AnnotatedElement element) {
    if (element instanceof Class<?>) {
      return (Class<?>) element;
    } else if (element instanceof Field) {
      return ((Field) element).getType();
    } else {
      throw new IllegalArgumentException(
          "Unsupported AnnotatedElement type: " + element.getClass());
    }
  }
}
