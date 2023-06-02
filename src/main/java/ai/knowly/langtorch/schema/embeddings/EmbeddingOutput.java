package ai.knowly.langtorch.schema.embeddings;

import ai.knowly.langtorch.schema.io.Output;
import java.util.List;

public class EmbeddingOutput implements Output {

  private final EmbeddingType type;

  private final List<Embedding> value;

  private EmbeddingOutput(EmbeddingType type, List<Embedding> value) {
    this.type = type;
    this.value = value;
  }

  public static EmbeddingOutput of(EmbeddingType type, List<Embedding> embeddings) {
    return new EmbeddingOutput(type, embeddings);
  }

  public EmbeddingType getType() {
    return type;
  }

  public List<Embedding> getValue() {
    return value;
  }
}
