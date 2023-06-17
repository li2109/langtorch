package ai.knowly.langtorch.llm.openai.schema.exception;

public class OpenAIServiceInterruptedException extends RuntimeException {
  public OpenAIServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
