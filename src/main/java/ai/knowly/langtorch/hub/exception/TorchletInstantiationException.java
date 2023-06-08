package ai.knowly.langtorch.hub.exception;

/** Exception thrown when a Torchlet cannot be instantiated. */
public class TorchletInstantiationException extends RuntimeException {
  public TorchletInstantiationException(Exception e) {
    super(e);
  }
}
