package ai.knowly.langtoch.prompt.template;

import ai.knowly.langtoch.schema.io.Input;
import ai.knowly.langtoch.schema.io.Output;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
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
public abstract class PromptTemplate implements Input, Output {
  public static final String VARIABLE_TEMPLATE_PATTERN = "\\{\\{\\$([a-zA-Z0-9_]+)\\}\\}";
  private static final String DEFAULT_EXAMPLE_HEADER = "Here's examples:\n";

  public static Builder builder() {
    return new AutoValue_PromptTemplate.Builder().setExamples(ImmutableList.of());
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

  private static Optional<String> formatExamples(
      List<String> examples, Optional<String> exampleHeader) {
    if (examples.isEmpty()) {
      return Optional.empty();
    }
    StringBuilder builder = new StringBuilder();
    if (exampleHeader.isPresent()) {
      if (!exampleHeader.get().endsWith("\n")) {
        builder.append(exampleHeader.get()).append("\n");
      } else {
        builder.append(exampleHeader.get());
      }
    } else {
      builder.append(DEFAULT_EXAMPLE_HEADER);
    }
    for (String example : examples) {
      builder.append(example).append("\n");
    }
    return Optional.of(builder.toString());
  }

  public abstract Builder toBuilder();

  public abstract Optional<String> template();

  // Example header is a string that can be used to describe the examples.
  public abstract Optional<String> exampleHeader();

  // Examples are a list of strings that can be used for few-shot prompting by providing examples of
  // the prompt.
  public abstract ImmutableList<String> examples();

  public abstract ImmutableMap<String, String> variables();

  // Public methods

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

  /**
   * Formats the template by replacing the variables with their values.
   *
   * @return The formatted template.
   */
  public String format() {
    validate();

    Optional<String> formattedExample = formatExamples(examples(), exampleHeader());
    if (variables().isEmpty()) {
      if (formattedExample.isPresent()) {
        return String.format("%s\n%s", template().get(), formattedExample.get());
      }
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
    if (formattedExample.isPresent()) {
      return String.format("%s\n%s", outputBuffer.toString(), formattedExample.get());
    }
    return outputBuffer.toString();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setTemplate(String template);

    public abstract Builder setExamples(List<String> examples);

    public abstract Builder setExampleHeader(String exampleHeader);

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
