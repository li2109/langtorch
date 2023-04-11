//package ai.knowly.langtoch.capability.glue;
//
//import ai.knowly.langtoch.capability.unit.BaseLLMCapabilityUnit;
//import com.google.common.collect.ImmutableList;
//import lombok.Builder;
//
///**
// * A sequential chain of capability units. Concatenating with capability units in sequence where the
// * output of one capability unit is the input of the next.
// *
// * <p>Currently, it only supports one variable in the prompt template.
// */
//@Builder(setterPrefix = "set")
//public class SequentialChainGlue {
//  private final ImmutableList<BaseLLMCapabilityUnit<?>> units;
//
//  public String run(String variableValue) {
//    if (units.isEmpty()) {
//      throw new RuntimeException("No capability unit to run.");
//    }
//    String result = variableValue;
//    for (BaseLLMCapabilityUnit<?> chain : units) {
//      result = chain.run();
//    }
//    return result;
//  }
//}
