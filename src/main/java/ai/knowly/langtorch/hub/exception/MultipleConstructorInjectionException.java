package ai.knowly.langtorch.hub.exception;

import ai.knowly.langtorch.hub.annotation.Inject;

/** Exception thrown when multiple constructors are annotated with {@link Inject}. */
public class MultipleConstructorInjectionException extends RuntimeException {
  public MultipleConstructorInjectionException(String message) {
    super(message);
  }
}
