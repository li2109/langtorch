package ai.knowly.langtoch.parser.output;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Iterables;
import java.util.Map;

/**
 * Output parser that takes a String output and converts it into a PromptTemplate with a single
 * variable filled in.
 */
@AutoValue
public abstract class StringToSingleVarPromptTemplateOutputParser
    extends StringOutputParser<PromptTemplate> {

  public static StringToSingleVarPromptTemplateOutputParser create(String singleVarTemplate) {
    return new AutoValue_StringToSingleVarPromptTemplateOutputParser(
        Map.of("template", singleVarTemplate));
  }

  abstract Map<Object, Object> context();

  /**
   * Parses the given String output into a PromptTemplate with a single variable filled in.
   *
   * @param output The String output to parse.
   * @return A PromptTemplate with a single variable filled in.
   */
  @Override
  public PromptTemplate parse(String output) {
    String template = (String) context().get("template");
    String variableName = Iterables.getOnlyElement(PromptTemplate.extractVariableNames(template));
    return PromptTemplate.builder()
        .setTemplate(template)
        .addVariableValuePair(variableName, output)
        .build();
  }

  @Override
  public Map<Object, Object> getContext() {
    return context();
  }
}
