package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
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
public class PromptTemplateTextCapabilityUnit
    extends CapabilityUnit<String, SingleText, SingleText, String> {
  private Optional<PromptTemplate> promptTemplate;

  private PromptTemplateTextCapabilityUnit(OpenAITextProcessor openAITextProcessor) {
    super(
        StringToSingleTextParser.create(), SingleTextToStringParser.create(), openAITextProcessor);
  }

  private PromptTemplateTextCapabilityUnit() {
    super(
        StringToSingleTextParser.create(),
        SingleTextToStringParser.create(),
        OpenAITextProcessor.create());
  }

  public static PromptTemplateTextCapabilityUnit create() {
    return new PromptTemplateTextCapabilityUnit();
  }

  public static PromptTemplateTextCapabilityUnit create(OpenAITextProcessor openAITextProcessor) {
    return new PromptTemplateTextCapabilityUnit(openAITextProcessor);
  }

  public PromptTemplateTextCapabilityUnit withPromptTemplate(PromptTemplate promptTemplate) {
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
