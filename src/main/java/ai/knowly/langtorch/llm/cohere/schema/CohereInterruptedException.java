package ai.knowly.langtorch.llm.cohere.schema;

public class CohereInterruptedException extends RuntimeException {
  public CohereInterruptedException(InterruptedException e) {
    super(e);
  }
}
