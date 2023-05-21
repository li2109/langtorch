package ai.knowly.langtorch.capability.module.openai;

import ai.knowly.langtorch.capability.modality.text.TextCompletionTextLLMCapability;
import ai.knowly.langtorch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtorch.parser.SingleTextToStringParser;
import ai.knowly.langtorch.parser.StringToSingleTextParser;
import ai.knowly.langtorch.prompt.template.PromptTemplate;
import java.util.Map;
import java.util.Optional;

/**
 * A capability unit that contains a prompt template and accepts a map of variable-value pairs to
 * the prompt template.
 */
public class PromptTemplateTextCapability extends TextCompletionTextLLMCapability<String, String> {
  private Optional<PromptTemplate> promptTemplate;

  private PromptTemplateTextCapability(OpenAITextProcessor openAITextProcessor) {
    super(openAITextProcessor);

    super.withInputParser(StringToSingleTextParser.create());
    super.withOutputParser(SingleTextToStringParser.create());
  }

  private PromptTemplateTextCapability() {
    super(OpenAITextProcessor.create());

    super.withInputParser(StringToSingleTextParser.create());
    super.withOutputParser(SingleTextToStringParser.create());
  }

  public static PromptTemplateTextCapability create() {
    return new PromptTemplateTextCapability();
  }

  public static PromptTemplateTextCapability create(OpenAITextProcessor openAITextProcessor) {
    return new PromptTemplateTextCapability(openAITextProcessor);
  }

  public PromptTemplateTextCapability withPromptTemplate(PromptTemplate promptTemplate) {
    this.promptTemplate = Optional.ofNullable(promptTemplate);
    return this;
  }

  public String run(Map<String, String> variableMap) {
    if (!promptTemplate.isPresent()) {
      throw new PromptTemplateNotSetException("Prompt template is not set");
    }
    return super.run(
        promptTemplate.get().toBuilder().addAllVariableValuePairs(variableMap).build().format());
  }
}
