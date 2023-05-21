package ai.knowly.langtorch.llm.integration.cohere.schema;

public class CohereInterruptedException extends RuntimeException {
  public CohereInterruptedException(InterruptedException e) {
    super(e);
  }
}
