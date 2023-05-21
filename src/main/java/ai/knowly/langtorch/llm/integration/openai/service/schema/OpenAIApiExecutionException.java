package ai.knowly.langtorch.llm.integration.openai.service.schema;

public class OpenAIApiExecutionException extends RuntimeException {
  public OpenAIApiExecutionException(Exception e) {
    super(e);
  }
}
