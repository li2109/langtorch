package ai.knowly.langtoch.capability.unit.wrapper;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.capability.unit.LLMCapabilityUnit;
import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
import ai.knowly.langtoch.parser.output.StringToSingleVarPromptTemplateOutputParser;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.auto.value.AutoValue;
import java.util.Map;

/**
 * A class wraps a LLM capability unit that processes a PromptTemplate and returns a transformed
 * PromptTemplate.
 */
@AutoValue
public abstract class TemplateToTemplateLLMWrapperUnit
    extends CapabilityUnit<PromptTemplate, PromptTemplate> {

  public static TemplateToTemplateLLMWrapperUnit create(
      BaseModel baseModel, Map<Object, Object> context) {
    LLMCapabilityUnit<PromptTemplate, PromptTemplate> capabilityUnit =
        LLMCapabilityUnit.<PromptTemplate, PromptTemplate>builder()
            .setModel(baseModel)
            .setInputParser(PromptTemplateStringInputParser.create())
            .setOutputParser(
                StringToSingleVarPromptTemplateOutputParser.create(
                    (String) context.get("template")))
            .build();

    return new AutoValue_TemplateToTemplateLLMWrapperUnit(capabilityUnit);
  }

  abstract LLMCapabilityUnit<PromptTemplate, PromptTemplate> capabilityUnit();

  /**
   * Executes the capability unit by processing the given PromptTemplate and returning a transformed
   * PromptTemplate.
   *
   * @param promptTemplate the input PromptTemplate
   * @return the transformed PromptTemplate
   */
  @Override
  public PromptTemplate run(PromptTemplate promptTemplate) {
    return capabilityUnit().run(promptTemplate);
  }
}
