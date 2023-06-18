package ai.knowly.langtorch.llm.huggingface.exception;

public class HuggingFaceExecutionException extends RuntimeException {
  public HuggingFaceExecutionException(Exception e) {
    super(e);
  }
}
