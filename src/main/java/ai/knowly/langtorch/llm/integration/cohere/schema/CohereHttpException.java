package ai.knowly.langtorch.llm.integration.cohere.schema;

public class CohereHttpException extends RuntimeException {
  public CohereHttpException(String msg, int statusCode, Exception parent) {
    super(msg, parent);
  }
}
