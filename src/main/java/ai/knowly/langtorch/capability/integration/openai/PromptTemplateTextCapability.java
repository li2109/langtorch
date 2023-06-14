package ai.knowly.langtorch.capability.integration.openai;

import ai.knowly.langtorch.capability.modality.text.Parsers;
import ai.knowly.langtorch.capability.modality.text.TextCompletionTextLLMCapability;
import ai.knowly.langtorch.preprocessing.parser.SingleTextToStringParser;
import ai.knowly.langtorch.preprocessing.parser.StringToSingleTextParser;
import ai.knowly.langtorch.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtorch.prompt.template.PromptTemplate;
import ai.knowly.langtorch.schema.text.SingleText;
import java.util.Map;
import javax.inject.Inject;

/**
 * A capability unit that contains a prompt template and accepts a map of variable-value pairs to
 * the prompt template.
 */
public class PromptTemplateTextCapability extends TextCompletionTextLLMCapability<String, String> {
  private final PromptTemplate promptTemplate;

  @Inject
  public PromptTemplateTextCapability(
      OpenAITextProcessor openAITextProcessor, PromptTemplate promptTemplate) {
    super(
        openAITextProcessor,
        Parsers.<String, SingleText, SingleText, String>builder()
            .setInputParser(StringToSingleTextParser.create())
            .setOutputParser(SingleTextToStringParser.create())
            .build());
    this.promptTemplate = promptTemplate;
  }

  public String run(Map<String, String> variableMap) {
    return super.run(
        promptTemplate.toBuilder().addAllVariableValuePairs(variableMap).build().format());
  }
}
