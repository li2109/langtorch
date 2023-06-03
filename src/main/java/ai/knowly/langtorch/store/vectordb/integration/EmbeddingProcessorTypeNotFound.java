package ai.knowly.langtorch.store.vectordb.integration;

/** Thrown when the embedding processor type is not found. */
public class EmbeddingProcessorTypeNotFound extends RuntimeException {
  public EmbeddingProcessorTypeNotFound(String message) {
    super(message);
  }
}
