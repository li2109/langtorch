package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
import ai.knowly.langtoch.parser.output.PassThroughStringOutputParser;
import ai.knowly.langtoch.prompt.PromptTemplate;

public class SimpleLLMCapabilityUnit {
  private final BaseLLMCapabilityUnit<PromptTemplate, String> capabilityUnit;

  SimpleLLMCapabilityUnit(BaseModel baseModel) {
    capabilityUnit =
        BaseLLMCapabilityUnit.<PromptTemplate, String>builder()
            .setModel(baseModel)
            .setInputParser(new PromptTemplateStringInputParser())
            .setOutputParser(new PassThroughStringOutputParser())
            .build();
  }

  public String run(PromptTemplate promptTemplate) {
    return capabilityUnit.run(promptTemplate);
  }
}
