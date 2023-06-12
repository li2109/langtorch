package ai.knowly.langtorch.hub.exception;

/** Exception thrown when multiple Torchlets are found associated with the class */
public class MultipleTorchletException extends RuntimeException {
  public MultipleTorchletException(String message) {
    super(message);
  }
}
