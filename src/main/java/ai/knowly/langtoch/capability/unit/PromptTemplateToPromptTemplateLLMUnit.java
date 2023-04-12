package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
import ai.knowly.langtoch.parser.output.StringToSingleVarPromptTemplateOutputParser;
import ai.knowly.langtoch.prompt.PromptTemplate;
import java.util.Map;

// Prompt Template -> Input Parser -> String
// -> Capability Unit
// -> String -> OutputParser -> PromptTemplate
public class PromptTemplateToPromptTemplateLLMUnit
    extends BaseCapabilityUnit<PromptTemplate, PromptTemplate> {
  private final BaseLLMCapabilityUnit<PromptTemplate, PromptTemplate> capabilityUnit;

  public PromptTemplateToPromptTemplateLLMUnit(BaseModel baseModel, Map<Object, Object> context) {
    capabilityUnit =
        BaseLLMCapabilityUnit.<PromptTemplate, PromptTemplate>builder()
            .setModel(baseModel)
            .setInputParser(new PromptTemplateStringInputParser())
            .setOutputParser(new StringToSingleVarPromptTemplateOutputParser(context))
            .build();
  }

  public PromptTemplate run(PromptTemplate promptTemplate) {
    return capabilityUnit.run(promptTemplate);
  }
}
