package ai.knowly.langtorch.llm.openai.schema.exception;

/**
 * The class defines a custom exception for interrupting OpenAI service.
 */
public class OpenAIServiceInterruptedException extends RuntimeException {
  public OpenAIServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
