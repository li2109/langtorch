package ai.knowly.langtoch.prompt;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class representing a prompt template with variables.
 *
 * <p>The template is a string with variables in the form of {{$var}}. The variables are replaced
 * with the values in the variables map.
 *
 * <p>Note: variables must be one or more word characters (letters, digits, or underscores).
 */
@AutoValue
public abstract class PromptTemplate {
  public static final String VARIABLE_TEMPLATE_PATTERN = "\\{\\{\\$([a-zA-Z0-9_]+)\\}\\}";

  public static Builder builder() {
    return new AutoValue_PromptTemplate.Builder();
  }

  public static ImmutableList<String> extractVariableNames(String template) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template);

    while (matcher.find()) {
      builder.add(matcher.group(1));
    }
    return builder.build();
  }

  public abstract Optional<String> template();

  public abstract ImmutableMap<String, String> variables();

  // Private methods

  /**
   * Validates the template and the variables map. <br>
   * 1. Template is not empty. <br>
   * 2. Number of variables in the template must match the number of variables in the map. <br>
   * 3. All variables in the template must be present in the variables map.
   */
  private void validate() {
    if (template().isEmpty()) {
      throw new IllegalArgumentException("Template is not present.");
    }

    ImmutableList<String> variableNamesFromTemplate = extractVariableNames(template().get());
    ImmutableMap<String, String> variablesInMap = variables();

    if (variableNamesFromTemplate.size() != variablesInMap.size()) {
      throw new IllegalArgumentException(
          "Number of variables in the template must match the number of variables in the map.");
    }

    variableNamesFromTemplate.forEach(
        variableName -> {
          if (!variablesInMap.containsKey(variableName)) {
            throw new IllegalArgumentException(
                String.format("Variable %s is not present in the variables map.", variableName));
          }
        });
  }

  // Public methods

  /**
   * Formats the template by replacing the variables with their values.
   *
   * @return The formatted template.
   */
  public String format() {
    validate();

    if (variables().isEmpty()) {
      return template().get();
    }

    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template().get());

    StringBuilder outputBuffer = new StringBuilder();
    while (matcher.find()) {
      String variableName = matcher.group(1);
      String replacement = variables().getOrDefault(variableName, "");
      matcher.appendReplacement(outputBuffer, Matcher.quoteReplacement(replacement));
    }
    matcher.appendTail(outputBuffer);
    return outputBuffer.toString();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setTemplate(String template);

    abstract ImmutableMap.Builder<String, String> variablesBuilder();

    public Builder addVariableValuePair(String variableName, String value) {
      variablesBuilder().put(variableName, value);
      return this;
    }

    public Builder addAllVariableValuePairs(Map<String, String> variables) {
      variablesBuilder().putAll(variables);
      return this;
    }

    public abstract PromptTemplate build();
  }
}
