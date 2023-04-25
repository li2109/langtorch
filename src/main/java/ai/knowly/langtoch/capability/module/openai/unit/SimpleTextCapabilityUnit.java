package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.parser.SingleTextToStringParser;
import ai.knowly.langtoch.parser.StringToSingleTextParser;

public class SimpleTextCapabilityUnit
    extends CapabilityUnit<String, SingleText, SingleText, String> {

  private SimpleTextCapabilityUnit(OpenAITextProcessor openAITextProcessor) {
    super(
        StringToSingleTextParser.create(), SingleTextToStringParser.create(), openAITextProcessor);
  }

  private SimpleTextCapabilityUnit() {
    super(
        StringToSingleTextParser.create(),
        SingleTextToStringParser.create(),
        OpenAITextProcessor.create());
  }

  public static SimpleTextCapabilityUnit create() {
    return new SimpleTextCapabilityUnit();
  }

  public static SimpleTextCapabilityUnit create(OpenAITextProcessor openAITextProcessor) {
    return new SimpleTextCapabilityUnit(openAITextProcessor);
  }
}
