package ai.knowly.langtorch.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ContextUtil {
  public static void setAccessible(Field field) {
    if (!Modifier.isPublic(field.getModifiers())) {
      field.setAccessible(true);
    }
  }
}
