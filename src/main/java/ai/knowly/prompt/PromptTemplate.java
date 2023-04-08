package ai.knowly.prompt;

import com.google.common.collect.ImmutableList;
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
  public static final String VARIABLE_TEMPLATE_PATTERN = "\\{\\{\\$([a-zA-Z0-9_]+)\\}\\}";
  private String template;
  // variable name must be one or more word characters (letters, digits, or underscores).
  private Map<String, String> variables;

  public PromptTemplate addVariableValuePair(String variableName, String value) {
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
      // Extract the variable name without the surrounding braces and dollar sign
      String variableName = matcher.group(1);
      // Retrieve the replacement, or use an empty string if the variable name is not
      String replacement = variables.getOrDefault(variableName, "");
      // found
      matcher.appendReplacement(
          outputBuffer,
          // Quote the replacement to avoid issues with special characters
          Matcher.quoteReplacement(replacement));
    }
    matcher.appendTail(outputBuffer);

    String result = outputBuffer.toString();
    return Optional.of(result);
  }

  public ImmutableList<String> extractVariableNames() {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template);

    while (matcher.find()) {
      builder.add(matcher.group(1));
    }
    return builder.build();
  }
}
