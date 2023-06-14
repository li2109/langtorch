package ai.knowly.langtorch.hub.exception;

/** Exception thrown when a class cannot be instantiated. */
public class ClassInstantiationException extends RuntimeException {
  public ClassInstantiationException(Exception e) {
    super(e);
  }
}
