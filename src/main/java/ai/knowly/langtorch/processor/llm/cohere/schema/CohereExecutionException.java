package ai.knowly.langtorch.processor.llm.cohere.schema;

import java.util.concurrent.ExecutionException;

public class CohereExecutionException extends RuntimeException {
  public CohereExecutionException(ExecutionException e) {
    super(e);
  }
}
