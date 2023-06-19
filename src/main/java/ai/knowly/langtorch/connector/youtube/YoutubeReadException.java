package ai.knowly.langtorch.connector.youtube;

/** Exception thrown when an error occurs during youtube reading. */
public class YoutubeReadException extends RuntimeException{
  public YoutubeReadException(String message, Throwable cause) {
    super(message, cause);
  }
  public YoutubeReadException(String message) {
    super(message);
  }
}
