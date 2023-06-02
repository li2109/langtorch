package ai.knowly.langtorch.llm.cohere.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generation {
  private String id;
  private String text;
  private List<TokenLikelihood> tokenLikelihoods;
  private Map<String, Object> dynamicFields;

  // common fields getters and setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<TokenLikelihood> getTokenLikelihoods() {
    return tokenLikelihoods;
  }

  public void setTokenLikelihoods(List<TokenLikelihood> tokenLikelihoods) {
    this.tokenLikelihoods = tokenLikelihoods;
  }

  // dynamic fields getters and setters
  public Object getField(String key) {
    if (dynamicFields != null) {
      return dynamicFields.get(key);
    }
    return null;
  }

  public void setField(String key, Object value) {
    if (dynamicFields == null) {
      dynamicFields = new HashMap<>();
    }
    dynamicFields.put(key, value);
  }
}
