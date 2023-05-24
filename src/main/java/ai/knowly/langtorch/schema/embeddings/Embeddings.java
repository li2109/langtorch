package ai.knowly.langtorch.schema.embeddings;

import ai.knowly.langtorch.schema.io.Output;
import java.util.List;

public class Embeddings implements Output {

  private final EmbeddingType type;

  private final List<Embedding> value;

  private Embeddings(EmbeddingType type, List<Embedding> value) {
    this.type = type;
    this.value = value;
  }

  public static Embeddings of(EmbeddingType type, List<Embedding> embeddings) {
    return new Embeddings(type, embeddings);
  }

  public EmbeddingType getType() {
    return type;
  }

  public List<Embedding> getValue() {
    return value;
  }
}
