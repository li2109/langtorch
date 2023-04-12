package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.prompt.PromptTemplate;
import java.util.Map;

public class PromptTemplateStringInputParser extends BaseStringInputParser<PromptTemplate> {

  @Override
  public String parse(PromptTemplate promptTemplate) {
    return promptTemplate.format();
  }

  @Override
  public Map<Object, Object> getContext() {
    return Map.of();
  }
}
