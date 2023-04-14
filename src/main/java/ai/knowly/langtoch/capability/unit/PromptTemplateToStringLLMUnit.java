package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
import ai.knowly.langtoch.prompt.template.PromptTemplate;

/**
 * A class representing a capability unit that processes a PromptTemplate and returns a transformed
 * String.
 */
public class PromptTemplateToStringLLMUnit extends CapabilityUnit<PromptTemplate, String> {
  private final LLMCapabilityUnit<PromptTemplate, String> capabilityUnit;

  /**
   * Creates a new PromptTemplateToStringLLMUnit with the specified base model.
   *
   * @param baseModel the base model used for processing
   */
  public PromptTemplateToStringLLMUnit(BaseModel baseModel) {
    capabilityUnit =
        LLMCapabilityUnit.<PromptTemplate, String>builder()
            .setModel(baseModel)
            .setInputParser(new PromptTemplateStringInputParser())
            .build();
  }

  /**
   * Executes the capability unit by processing the given PromptTemplate and returning a transformed
   * String.
   *
   * @param promptTemplate the input PromptTemplate
   * @return the transformed String
   */
  @Override
  public String run(PromptTemplate promptTemplate) {
    return capabilityUnit.run(promptTemplate);
  }
}
