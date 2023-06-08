package ai.knowly.langtorch.hub.exception;

import ai.knowly.langtorch.hub.annotation.TorchInject;

/** Exception thrown when multiple constructors are annotated with {@link TorchInject}. */
public class MultipleConstructorInjectionException extends RuntimeException {
  public MultipleConstructorInjectionException(String message) {
    super(message);
  }
}
