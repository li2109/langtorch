package ai.knowly.langtorch.processor.llm.cohere.schema;

public class CohereHttpException extends RuntimeException {
  public CohereHttpException(String msg, Exception parent) {
    super(msg, parent);
  }
}
