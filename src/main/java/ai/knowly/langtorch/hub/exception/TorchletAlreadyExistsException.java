package ai.knowly.langtorch.hub.exception;

/** Exception thrown when a torchlet already exists. */
public class TorchletAlreadyExistsException extends RuntimeException {
  public TorchletAlreadyExistsException(String e) {
    super(e);
  }
}
