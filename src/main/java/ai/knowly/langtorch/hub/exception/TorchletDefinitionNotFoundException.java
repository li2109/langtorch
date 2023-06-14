package ai.knowly.langtorch.hub.exception;

/** Exception thrown when a Torchlet definition is not found. */
public class TorchletDefinitionNotFoundException extends RuntimeException {
  public TorchletDefinitionNotFoundException(String message) {
    super(message);
  }
}
