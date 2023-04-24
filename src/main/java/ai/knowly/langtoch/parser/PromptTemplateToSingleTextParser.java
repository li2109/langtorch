package ai.knowly.langtoch.parser;

import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.prompt.template.PromptTemplate;

public class PromptTemplateToSingleTextParser extends Parser<PromptTemplate, SingleText> {

  private PromptTemplateToSingleTextParser() {
    super();
  }

  public static PromptTemplateToSingleTextParser create() {
    return new PromptTemplateToSingleTextParser();
  }

  @Override
  public SingleText parse(PromptTemplate input) {
    return SingleText.of(input.format());
  }
}
