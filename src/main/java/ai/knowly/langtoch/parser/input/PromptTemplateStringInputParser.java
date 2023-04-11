package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.prompt.PromptTemplate;

public class PromptTemplateStringInputParser extends BaseStringInputParser<PromptTemplate> {
  @Override
  public String parse(PromptTemplate promptTemplate) {
    return promptTemplate.format();
  }
}
