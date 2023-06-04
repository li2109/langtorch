package ai.knowly.langtorch.llm.openai.schema;

public class OpenAIServiceInterruptedException extends RuntimeException {
  public OpenAIServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
