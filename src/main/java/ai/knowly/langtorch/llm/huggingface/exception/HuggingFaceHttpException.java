package ai.knowly.langtorch.llm.huggingface.exception;

public class HuggingFaceHttpException extends RuntimeException {
  public HuggingFaceHttpException(String e) {
    super(e);
  }
}
