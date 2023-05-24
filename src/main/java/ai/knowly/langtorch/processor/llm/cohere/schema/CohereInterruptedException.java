package ai.knowly.langtorch.processor.llm.cohere.schema;

public class CohereInterruptedException extends RuntimeException {
  public CohereInterruptedException(InterruptedException e) {
    super(e);
  }
}
