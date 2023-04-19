package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.auto.value.AutoValue;
import java.util.Map;

/** Input parser that takes a PromptTemplate object and formats it into a String. */
@AutoValue
public abstract class PromptTemplateStringInputParser extends StringInputParser<PromptTemplate> {

  public static PromptTemplateStringInputParser create() {
    return new AutoValue_PromptTemplateStringInputParser();
  }

  /**
   * Formats the given PromptTemplate by replacing the variables with their values.
   *
   * @param promptTemplate The PromptTemplate object to format.
   * @return The formatted template as a String.
   */
  @Override
  public String parse(PromptTemplate promptTemplate) {
    return promptTemplate.format();
  }

  @Override
  public Map<Object, Object> getContext() {
    return Map.of();
  }
}
