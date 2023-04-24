package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnitWithParser;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.parser.PromptTemplateToSingleTextParser;
import ai.knowly.langtoch.prompt.template.PromptTemplate;

public class PromptTemplateTextCapabilityUnit
    extends CapabilityUnitWithParser<PromptTemplate, SingleText, SingleText, SingleText> {

  private PromptTemplateTextCapabilityUnit(OpenAITextProcessor openAITextProcessor) {
    super(openAITextProcessor);
    super.withInputParser(PromptTemplateToSingleTextParser.create());
  }

  private PromptTemplateTextCapabilityUnit() {
    super(OpenAITextProcessor.create());
    super.withInputParser(PromptTemplateToSingleTextParser.create());
  }

  public static PromptTemplateTextCapabilityUnit create() {
    return new PromptTemplateTextCapabilityUnit();
  }

  public static PromptTemplateTextCapabilityUnit create(OpenAITextProcessor openAITextProcessor) {
    return new PromptTemplateTextCapabilityUnit(openAITextProcessor);
  }

  @Override
  public SingleText run(PromptTemplate input) {
    return super.run(input);
  }
}
