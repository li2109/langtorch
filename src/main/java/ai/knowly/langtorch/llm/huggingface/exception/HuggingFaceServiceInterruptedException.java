package ai.knowly.langtorch.llm.huggingface.exception;

public class HuggingFaceServiceInterruptedException extends RuntimeException {
  public HuggingFaceServiceInterruptedException(InterruptedException e) {
    super(e);
  }
}
