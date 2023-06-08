package ai.knowly.langtorch.hub.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Inject {
  // The name of the torchlet. If not specified, the name of the class will be used. It should be
  // set when there are multiple torchlets of the same class.
  String value() default "";
}
