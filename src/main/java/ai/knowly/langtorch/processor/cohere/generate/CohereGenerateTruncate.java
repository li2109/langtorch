package ai.knowly.langtorch.processor.cohere.generate;

/** Specifies how the API will handle inputs longer than the maximum token length. */
public enum CohereGenerateTruncate {
  NONE("NONE"),
  END("END"),
  START("START");

  private final String truncate;

  CohereGenerateTruncate(String truncate) {
    this.truncate = truncate;
  }

  @Override
  public String toString() {
    return truncate;
  }
}
