package ai.knowly.langtorch.llm.openai.schema;

public class OpenAIApiExecutionException extends RuntimeException {
  public OpenAIApiExecutionException(Exception e) {
    super(e);
  }
}
