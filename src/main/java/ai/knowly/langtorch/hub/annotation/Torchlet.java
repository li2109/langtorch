package ai.knowly.langtorch.hub.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Torchlet {
  String value() default "";
}
