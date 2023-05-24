package ai.knowly.langtorch.processor.llm.openai.service.schema;

public class OpenAIServiceInterruptedException extends RuntimeException {
  public OpenAIServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
