package ai.knowly.langtorch.processor.openai.chat;

public class UnknownMessageException extends RuntimeException {
  public UnknownMessageException(String message) {
    super(message);
  }
}
