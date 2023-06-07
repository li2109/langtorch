package ai.knowly.langtorch.hub.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TorchHub is a marker annotation for the main class of a torchlet.
@Retention(RetentionPolicy.RUNTIME)
public @interface TorchHub {
  // The top-level package name to be scanned for torchlets.
  String value() default "";
}
