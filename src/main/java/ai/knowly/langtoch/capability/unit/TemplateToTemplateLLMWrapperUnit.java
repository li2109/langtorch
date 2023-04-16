package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
import ai.knowly.langtoch.parser.output.StringToSingleVarPromptTemplateOutputParser;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import java.util.Map;

/**
 * A class representing a capability unit that processes a PromptTemplate and returns a transformed
 * PromptTemplate.
 */
public class TemplateToTemplateLLMWrapperUnit
    extends CapabilityUnit<PromptTemplate, PromptTemplate> {

  private final LLMCapabilityUnit<PromptTemplate, PromptTemplate> capabilityUnit;

  /**
   * Creates a new PromptTemplateToPromptTemplateLLMUnit with the specified base model and context.
   *
   * @param baseModel the base model used for processing
   * @param context a map containing context information
   */
  public TemplateToTemplateLLMWrapperUnit(BaseModel baseModel, Map<Object, Object> context) {
    capabilityUnit =
        LLMCapabilityUnit.<PromptTemplate, PromptTemplate>builder()
            .setModel(baseModel)
            .setInputParser(new PromptTemplateStringInputParser())
            .setOutputParser(new StringToSingleVarPromptTemplateOutputParser(context))
            .build();
  }

  /**
   * Executes the capability unit by processing the given PromptTemplate and returning a transformed
   * PromptTemplate.
   *
   * @param promptTemplate the input PromptTemplate
   * @return the transformed PromptTemplate
   */
  @Override
  public PromptTemplate run(PromptTemplate promptTemplate) {
    return capabilityUnit.run(promptTemplate);
  }
}
