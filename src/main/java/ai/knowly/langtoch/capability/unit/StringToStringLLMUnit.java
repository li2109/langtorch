package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.base.BaseModel;
import ai.knowly.langtoch.parser.input.PassThroughStringInputParser;
import ai.knowly.langtoch.parser.output.PassThroughStringOutputParser;

public class StringToStringLLMUnit extends BaseCapabilityUnit<String, String> {
  private final BaseLLMCapabilityUnit<String, String> capabilityUnit;

  StringToStringLLMUnit(BaseModel baseModel) {
    capabilityUnit =
        BaseLLMCapabilityUnit.<String, String>builder()
            .setModel(baseModel)
            .setInputParser(new PassThroughStringInputParser())
            .setOutputParser(new PassThroughStringOutputParser())
            .build();
  }

  public String run(String input) {
    return capabilityUnit.run(input);
  }
}
