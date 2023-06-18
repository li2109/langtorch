package ai.knowly.langtorch.schema.embeddings;

/**
 * @author maxiao
 * @date 2023/06/17
 */
public enum MiniMaxEmbeddingTypeScene {

  /** Used to generate vectors for queries */
  DB("db"),
  /** retrieving text */
  QUERY("query"),
  ;

  private String value;

  MiniMaxEmbeddingTypeScene(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
