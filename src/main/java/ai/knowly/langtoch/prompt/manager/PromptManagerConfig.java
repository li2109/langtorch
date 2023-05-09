package ai.knowly.langtoch.prompt.manager;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class PromptManagerConfig {
  @SerializedName("promptTemplates")
  private Map<Long, PromptTemplate> promptTemplates;

  private PromptManagerConfig(Map<Long, PromptTemplate> promptTemplates) {
    this.promptTemplates = promptTemplates;
  }

  public static PromptManagerConfig create(Map<Long, PromptTemplate> promptTemplates) {
    return new PromptManagerConfig(promptTemplates);
  }

  public Map<Long, PromptTemplate> getPromptTemplates() {
    return promptTemplates;
  }
}
