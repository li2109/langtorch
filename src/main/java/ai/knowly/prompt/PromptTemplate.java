package ai.knowly.prompt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

/**
 * A template for a prompt. The template is a string with variables in the form of {variableName}.
 * The variables are replaced with the values in the variables map.
 */
@Data
@Builder(setterPrefix = "set")
public class PromptTemplate {
  private String template;
  private Map<String, String> variables;

  public PromptTemplate addVariable(String variableName, String value) {
    if (variables == null) {
      variables = new HashMap<>();
    }
    variables.put(variableName, value);
    return this;
  }

  /**
   * Formats the template by replacing the variables with their values.
   *
   * @return The formatted template.
   */
  public Optional<String> format() {
    if (template == null) {
      return Optional.empty();
    }
    // No variable == simple prompt.
    if (variables == null) {
      return Optional.of(template);
    }
    String result = template;
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      String variablePattern = "\\{" + entry.getKey() + "\\}";
      result = result.replaceAll(variablePattern, entry.getValue());
    }
    return Optional.of(result);
  }
}
