package ai.knowly.langtorch.hub.schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TorchScope {
  TorchScopeValue value() default TorchScopeValue.SINGLETON;
}
