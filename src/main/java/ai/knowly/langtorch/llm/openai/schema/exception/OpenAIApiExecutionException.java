package ai.knowly.langtorch.llm.openai.schema.exception;

/**
 * The class defines a custom exception for errors that occur during the execution of OpenAI API.
 */
public class OpenAIApiExecutionException extends RuntimeException {
  public OpenAIApiExecutionException(Exception e) {
    super(e);
  }
}
