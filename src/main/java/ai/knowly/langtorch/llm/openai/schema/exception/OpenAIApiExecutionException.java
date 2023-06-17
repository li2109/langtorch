package ai.knowly.langtorch.llm.openai.schema.exception;

public class OpenAIApiExecutionException extends RuntimeException {
  public OpenAIApiExecutionException(Exception e) {
    super(e);
  }
}
