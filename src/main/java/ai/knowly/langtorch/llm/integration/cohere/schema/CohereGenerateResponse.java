package ai.knowly.langtorch.llm.integration.cohere.schema;

import java.util.*;

public class CohereGenerateResponse {
  private String id;
  private List<Generation> generations;
  private List<String> warnings;
  private Map<String, Object> dynamicFields;

  // common fields getters and setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Generation> getGenerations() {
    return generations;
  }

  public void setGenerations(List<Generation> generations) {
    this.generations = generations;
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
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
