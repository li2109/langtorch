package ai.knowly.langtorch.utils.api.key;

/** Thrown when a key is not found in the .env file. */
public class KeyNotFoundException extends RuntimeException {
  public KeyNotFoundException(String message) {
    super(message);
  }
}
