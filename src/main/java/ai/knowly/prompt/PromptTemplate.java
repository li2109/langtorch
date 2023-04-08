package ai.knowly.prompt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Data;

/**
 * A template for a prompt. The template is a string with variables in the form of {variableName}.
 * The variables are replaced with the values in the variables map.
 */
@Data
@Builder(setterPrefix = "set")
public class PromptTemplate {
  public static final String VARIABLE_TEMPLATE_PATTERN = "\\{\\{\\$(\\w+)\\}\\}";
  private String template;
  // variable name must be one or more word characters (letters, digits, or underscores).
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

    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template);

    StringBuilder outputBuffer = new StringBuilder();
    while (matcher.find()) {
      String variableName =
          matcher.group(
              1); // Extract the variable name without the surrounding braces and dollar sign
      String replacement =
          variables.getOrDefault(
              variableName,
              ""); // Retrieve the replacement, or use an empty string if the variable name is not
      // found
      matcher.appendReplacement(
          outputBuffer,
          Matcher.quoteReplacement(
              replacement)); // Quote the replacement to avoid issues with special characters
    }
    matcher.appendTail(outputBuffer);

    String result = outputBuffer.toString();
    return Optional.of(result);
  }
}
