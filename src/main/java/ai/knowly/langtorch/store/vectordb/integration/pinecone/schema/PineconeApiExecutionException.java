package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema;

import java.util.concurrent.ExecutionException;

public class PineconeApiExecutionException extends RuntimeException {
  public PineconeApiExecutionException(ExecutionException e) {
    super(e);
  }
}
