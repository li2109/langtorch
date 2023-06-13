package ai.knowly.langtorch.capability.module.openai;

import ai.knowly.langtorch.capability.modality.text.TextCompletionTextLLMCapability;
import ai.knowly.langtorch.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtorch.preprocessing.parser.SingleTextToStringParser;
import ai.knowly.langtorch.preprocessing.parser.StringToSingleTextParser;

/** A simple text capability unit that leverages openai api to generate response */
public class SimpleTextCapability extends TextCompletionTextLLMCapability<String, String> {

  private SimpleTextCapability(OpenAITextProcessor openAITextProcessor) {
    super(openAITextProcessor);
    super.withInputParser(StringToSingleTextParser.create());
    super.withOutputParser(SingleTextToStringParser.create());
  }

  private SimpleTextCapability() {
    super(OpenAITextProcessor.create());

    super.withInputParser(StringToSingleTextParser.create());
    super.withOutputParser(SingleTextToStringParser.create());
  }

  public static SimpleTextCapability create() {
    return new SimpleTextCapability();
  }

  public static SimpleTextCapability create(String openAIKey) {
    return new SimpleTextCapability(OpenAITextProcessor.create(openAIKey));
  }

  public static SimpleTextCapability create(OpenAITextProcessor openAITextProcessor) {
    return new SimpleTextCapability(openAITextProcessor);
  }
}
