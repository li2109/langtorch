package ai.knowly.langtoch.parser.output;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.collect.Iterables;
import java.util.Map;

/**
 * Output parser that takes a String output and converts it into a PromptTemplate with a single
 * variable filled in.
 */
public class StringToSingleVarPromptTemplateOutputParser
    extends StringOutputParser<PromptTemplate> {

  private final Map<Object, Object> context;

  /**
   * Constructs a new StringToSingleVarPromptTemplateOutputParser with the given context.
   *
   * @param context A map containing the context for the output parser.
   */
  public StringToSingleVarPromptTemplateOutputParser(Map<Object, Object> context) {
    this.context = context;
  }

  /**
   * Parses the given String output into a PromptTemplate with a single variable filled in.
   *
   * @param output The String output to parse.
   * @return A PromptTemplate with a single variable filled in.
   */
  @Override
  public PromptTemplate parse(String output) {
    String template = (String) context.get("template");
    String variableName = Iterables.getOnlyElement(PromptTemplate.extractVariableNames(template));
    return PromptTemplate.builder()
        .setTemplate(template)
        .addVariableValuePair(variableName, output)
        .build();
  }

  @Override
  public Map<Object, Object> getContext() {
    return this.context;
  }
}
