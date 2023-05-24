package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema;

public class PineconeInterruptedException extends RuntimeException {
  public PineconeInterruptedException(InterruptedException e) {
    super(e);
  }
}
