package ai.knowly.langtorch.llm.cohere.schema;

public class CohereHttpException extends RuntimeException {
  public CohereHttpException(String msg, Exception parent) {
    super(msg, parent);
  }
}
