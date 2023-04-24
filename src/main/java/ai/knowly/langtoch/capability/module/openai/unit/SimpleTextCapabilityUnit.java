package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnitWithParser;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessorConfig;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.parser.SingleTextToStringParser;
import ai.knowly.langtoch.parser.StringToSingleTextParser;

public class SimpleTextCapabilityUnit
    extends CapabilityUnitWithParser<String, SingleText, SingleText, String> {
  private SimpleTextCapabilityUnit(OpenAITextProcessorConfig openAITextProcessorConfig) {
    super(OpenAITextProcessor.create().withConfig(openAITextProcessorConfig));
    super.withInputParser(StringToSingleTextParser.create());
    super.withInputParser(StringToSingleTextParser.create());
    super.withOutputParser(SingleTextToStringParser.create());
  }

  private SimpleTextCapabilityUnit() {
    super(OpenAITextProcessor.create());
    super.withInputParser(StringToSingleTextParser.create());
    super.withOutputParser(SingleTextToStringParser.create());
  }

  public static SimpleTextCapabilityUnit create() {
    return new SimpleTextCapabilityUnit();
  }

  public static SimpleTextCapabilityUnit create(
      OpenAITextProcessorConfig openAITextProcessorConfig) {
    return new SimpleTextCapabilityUnit(openAITextProcessorConfig);
  }

  @Override
  protected String run(String input) {
    return super.run(input);
  }
}
