package ai.knowly.langtoch.capability.module.openai;

import ai.knowly.langtoch.capability.modality.text.TextCompletionTextLLMCapability;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.parser.SingleTextToStringParser;
import ai.knowly.langtoch.parser.StringToSingleTextParser;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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

  public String run(Map<String, String> variableMap)
      throws ExecutionException, InterruptedException {
    if (promptTemplate.isEmpty()) {
      throw new RuntimeException("Prompt template is not set");
    }
    return super.run(
        promptTemplate.get().toBuilder().addAllVariableValuePairs(variableMap).build().format());
  }
}
