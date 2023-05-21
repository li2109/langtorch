package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema;

public class PineconeHttpParseException extends RuntimeException {
  public PineconeHttpParseException(String msg) {
    super(msg);
  }
}
