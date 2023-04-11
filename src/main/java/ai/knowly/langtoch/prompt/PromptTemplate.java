package ai.knowly.langtoch.prompt;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import java.util.Map;
import java.util.Optional;

/**
 * A template for a prompt. The template is a string with variables in the form of {{$var}}. The
 * variables are replaced with the values in the variables map.
 *
 * <p>Note: variables must be one or more word characters (letters, digits, or underscores).
 */
@AutoValue
public abstract class PromptTemplate {
  public static final String VARIABLE_TEMPLATE_PATTERN = "\\{\\{\\$([a-zA-Z0-9_]+)\\}\\}";

  public static Builder builder() {
    return new AutoValue_PromptTemplate.Builder();
  }

  public abstract Optional<String> template();

  public abstract ImmutableMap<String, String> variables();

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

    ImmutableList<String> variableNameFromTemplate = extractVariableNames();
    ImmutableMap<String, String> variablesInMap = variables();

    // Number of variables in the template must match the number of variables in the map.
    if (variableNameFromTemplate.size() != variablesInMap.size()) {
      throw new IllegalArgumentException(
          "Number of variables in the template must match the number of variables in the map.");
    }

    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template().get());
    while (matcher.find()) {
      String variableName = matcher.group(1);
      if (!variablesInMap.containsKey(variableName)) {
        throw new IllegalArgumentException(
            String.format("Variable %s is not present in the variables map.", variableName));
      }
    }
  }

  /**
   * Formats the template by replacing the variables with their values.
   *
   * @return The formatted template.
   */
  public String format() {
    validate();

    // If the variables map is empty (as we have done validation above, variables in the map match
    // exactly the variables in the template), return the template as is.
    if (variables().isEmpty()) {
      return template().get();
    }

    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template().get());

    StringBuilder outputBuffer = new StringBuilder();
    while (matcher.find()) {
      // Extract the variable name without the surrounding braces and dollar sign
      String variableName = matcher.group(1);
      // Retrieve the replacement, or use an empty string if the variable name is not
      String replacement = variables().getOrDefault(variableName, "");
      // found
      matcher.appendReplacement(
          outputBuffer,
          // Quote the replacement to avoid issues with special characters
          Matcher.quoteReplacement(replacement));
    }
    matcher.appendTail(outputBuffer);
    return outputBuffer.toString();
  }

  public ImmutableList<String> extractVariableNames() {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    Pattern compiledPattern = Pattern.compile(VARIABLE_TEMPLATE_PATTERN);
    Matcher matcher = compiledPattern.matcher(template().get());

    while (matcher.find()) {
      builder.add(matcher.group(1));
    }
    return builder.build();
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
