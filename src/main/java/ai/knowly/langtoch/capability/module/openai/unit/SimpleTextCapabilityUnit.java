package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.modality.text.TextCompletionTextLLMCapability;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.parser.SingleTextToStringParser;
import ai.knowly.langtoch.parser.StringToSingleTextParser;
import java.util.Optional;

/** A simple text capability unit that leverages openai api to generate response */
public class SimpleTextCapabilityUnit extends TextCompletionTextLLMCapability<String, String> {

  private SimpleTextCapabilityUnit(OpenAITextProcessor openAITextProcessor) {
    super(
        openAITextProcessor,
        Optional.of(StringToSingleTextParser.create()),
        Optional.of(SingleTextToStringParser.create()),
        String.class);
  }

  private SimpleTextCapabilityUnit() {
    super(
        OpenAITextProcessor.create(),
        Optional.of(StringToSingleTextParser.create()),
        Optional.of(SingleTextToStringParser.create()),
        String.class);
  }

  public static SimpleTextCapabilityUnit create() {
    return new SimpleTextCapabilityUnit();
  }

  public static SimpleTextCapabilityUnit create(String openAIKey) {
    return new SimpleTextCapabilityUnit(OpenAITextProcessor.create(openAIKey));
  }

  public static SimpleTextCapabilityUnit create(OpenAITextProcessor openAITextProcessor) {
    return new SimpleTextCapabilityUnit(openAITextProcessor);
  }
}
