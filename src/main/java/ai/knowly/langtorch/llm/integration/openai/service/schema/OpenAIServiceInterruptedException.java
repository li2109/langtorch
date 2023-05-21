package ai.knowly.langtorch.llm.integration.openai.service.schema;

public class OpenAIServiceInterruptedException extends RuntimeException {
  public OpenAIServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
