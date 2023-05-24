package ai.knowly.langtorch.processor.module.openai.chat;

public class UnknownMessageException extends RuntimeException {
  public UnknownMessageException(String message) {
    super(message);
  }
}
