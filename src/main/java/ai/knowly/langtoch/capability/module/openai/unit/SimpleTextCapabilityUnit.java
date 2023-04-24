package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;

public class SimpleTextCapabilityUnit extends CapabilityUnit<SingleText, SingleText> {

  private SimpleTextCapabilityUnit(OpenAITextProcessor openAITextProcessor) {
    super(openAITextProcessor);
  }

  private SimpleTextCapabilityUnit() {
    super(OpenAITextProcessor.create());
  }

  public static SimpleTextCapabilityUnit create() {
    return new SimpleTextCapabilityUnit();
  }

  public static SimpleTextCapabilityUnit create(OpenAITextProcessor openAITextProcessor) {
    return new SimpleTextCapabilityUnit(openAITextProcessor);
  }

  @Override
  public SingleText run(SingleText input) {
    return super.run(input);
  }
}
