package ai.knowly.langtoch.schema.embeddings;

import ai.knowly.langtoch.schema.io.Output;

import java.util.List;

public class Embeddings implements Output {

  private final EmbeddingType type;

  private final List<Embedding> embeddings;

  private Embeddings(EmbeddingType type, List<Embedding> embeddings) {
    this.type = type;
    this.embeddings = embeddings;
  }

  public static Embeddings of(EmbeddingType type, List<Embedding> embeddings) {
    return new Embeddings(type, embeddings);
  }

  public EmbeddingType getType() {
    return type;
  }

  public List<Embedding> getEmbeddings() {
    return embeddings;
  }
}
