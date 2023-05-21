package ai.knowly.langtorch.llm.integration.cohere.schema;

import java.util.concurrent.ExecutionException;

public class CohereExecutionException extends RuntimeException {
  public CohereExecutionException(ExecutionException e) {
    super(e);
  }
}
