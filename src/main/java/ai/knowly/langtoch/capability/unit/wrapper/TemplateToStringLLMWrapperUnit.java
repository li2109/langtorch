package ai.knowly.langtoch.capability.unit.wrapper;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.capability.unit.LLMCapabilityUnit;
import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
import ai.knowly.langtoch.prompt.template.PromptTemplate;

/**
 * A class wraps a LLM capability unit that processes a PromptTemplate and returns a transformed
 * String.
 */
public class TemplateToStringLLMWrapperUnit extends CapabilityUnit<PromptTemplate, String> {
  private final LLMCapabilityUnit<PromptTemplate, String> capabilityUnit;

  /**
   * Creates a new PromptTemplateToStringLLMUnit with the specified base model.
   *
   * @param baseModel the base model used for processing
   */
  public TemplateToStringLLMWrapperUnit(BaseModel baseModel) {
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
