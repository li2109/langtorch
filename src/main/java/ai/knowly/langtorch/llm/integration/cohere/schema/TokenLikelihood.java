package ai.knowly.langtorch.llm.integration.cohere.schema;

public class TokenLikelihood {
  private String token;
  private double likelihood;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public double getLikelihood() {
    return likelihood;
  }

  public void setLikelihood(double likelihood) {
    this.likelihood = likelihood;
  }
}
