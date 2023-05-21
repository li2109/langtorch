package ai.knowly.langtorch.capability.module.openai;

public class PromptTemplateNotSetException extends RuntimeException {
  public PromptTemplateNotSetException(String message) {
    super(message);
  }
}
