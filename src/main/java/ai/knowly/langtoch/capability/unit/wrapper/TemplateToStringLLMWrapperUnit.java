//package ai.knowly.langtoch.capability.unit.wrapper;
//
//import ai.knowly.langtoch.capability.unit.CapabilityUnit;
//import ai.knowly.langtoch.capability.unit.LLMCapabilityUnit;
//import ai.knowly.langtoch.llm.processor.Processor;
//import ai.knowly.langtoch.parser.input.PromptTemplateStringInputParser;
//import ai.knowly.langtoch.prompt.template.PromptTemplate;
//import com.google.auto.value.AutoValue;
//
///**
// * A class wraps a LLM capability unit that processes a PromptTemplate and returns a transformed
// * String.
// */
//@AutoValue
//public abstract class TemplateToStringLLMWrapperUnit
//    extends CapabilityUnit<PromptTemplate, String> {
//
//  public static TemplateToStringLLMWrapperUnit create(Processor processor) {
//    LLMCapabilityUnit<PromptTemplate, String> capabilityUnit =
//        LLMCapabilityUnit.<PromptTemplate, String>builder()
//            .setModel(processor)
//            .setInputParser(PromptTemplateStringInputParser.create())
//            .build();
//
//    return new AutoValue_TemplateToStringLLMWrapperUnit(capabilityUnit);
//  }
//
//  abstract LLMCapabilityUnit<PromptTemplate, String> capabilityUnit();
//
//  /**
//   * Executes the capability unit by processing the given PromptTemplate and returning a transformed
//   * String.
//   *
//   * @param promptTemplate the input PromptTemplate
//   * @return the transformed String
//   */
//  @Override
//  public String run(PromptTemplate promptTemplate) {
//    return capabilityUnit().run(promptTemplate);
//  }
//}
