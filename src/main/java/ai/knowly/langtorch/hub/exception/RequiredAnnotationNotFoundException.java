package ai.knowly.langtorch.hub.exception;

public class RequiredAnnotationNotFoundException extends RuntimeException {
  public RequiredAnnotationNotFoundException(String message) {
    super(message);
  }
}
