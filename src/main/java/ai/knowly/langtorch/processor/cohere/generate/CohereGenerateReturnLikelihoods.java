package ai.knowly.langtorch.processor.cohere.generate;

/** Specifies how and if the token likelihoods are returned with the response. */
public enum CohereGenerateReturnLikelihoods {
  NONE("NONE"),
  ALL("ALL"),
  GENERATION("GENERATION");

  private final String returnLikelihoods;

  CohereGenerateReturnLikelihoods(String returnLikelihoods) {
    this.returnLikelihoods = returnLikelihoods;
  }

  @Override
  public String toString() {
    return returnLikelihoods;
  }
}
