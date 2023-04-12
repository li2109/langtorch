package ai.knowly.langtoch.parser.output;

import ai.knowly.langtoch.prompt.PromptTemplate;
import com.google.common.collect.Iterables;
import java.util.Map;

public class StringToSingleVarPromptTemplateOutputParser
    extends BaseStringOutputParser<PromptTemplate> {

  private final Map<Object, Object> context;

  public StringToSingleVarPromptTemplateOutputParser(Map<Object, Object> context) {
    this.context = context;
  }

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
