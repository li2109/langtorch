package ai.knowly.langtorch.capability.module.openai;

import ai.knowly.langtorch.capability.modality.text.Parsers;
import ai.knowly.langtorch.capability.modality.text.TextCompletionTextLLMCapability;
import ai.knowly.langtorch.preprocessing.parser.SingleTextToStringParser;
import ai.knowly.langtorch.preprocessing.parser.StringToSingleTextParser;
import ai.knowly.langtorch.processor.module.openai.text.OpenAITextProcessor;
import ai.knowly.langtorch.schema.text.SingleText;
import javax.inject.Inject;

/** A simple text capability unit that leverages openai api to generate response */
public class SimpleTextCapability extends TextCompletionTextLLMCapability<String, String> {
  @Inject
  public SimpleTextCapability(OpenAITextProcessor openAITextProcessor) {
    super(
        openAITextProcessor,
        Parsers.<String, SingleText, SingleText, String>builder()
            .setInputParser(StringToSingleTextParser.create())
            .setOutputParser(SingleTextToStringParser.create())
            .build());
  }
}
