//package ai.knowly.langtoch.capability.glue;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SequentialChain {
//    private List<BaseLLMCapabilityUnit<?>> capabilityUnits;
//
//    public SequentialChain() {
//        capabilityUnits = new ArrayList<>();
//    }
//
//    public <T, R> void addCapabilityUnit(BaseLLMCapabilityUnit<T, R> capabilityUnit) {
//        if (!capabilityUnits.isEmpty()) {
//            BaseLLMCapabilityUnit<?, ?> lastUnit = capabilityUnits.get(capabilityUnits.size() - 1);
//            if (!lastUnit.getOutputType().equals(capabilityUnit.getInputType())) {
//                throw new IllegalArgumentException("Input type of the new unit must match the output type of the previous unit");
//            }
//        }
//        capabilityUnits.add(capabilityUnit);
//    }
//
//    public <T, R> R execute(T input) {
//        Object currentInput = input;
//        for (BaseLLMCapabilityUnit<?, ?> capabilityUnit : capabilityUnits) {
//            currentInput = capabilityUnit.run(currentInput);
//        }
//        return (R) currentInput;
//    }
//}
//
//public abstract class BaseLLMCapabilityUnit<T, R> {
//    private Class<T> inputType;
//    private Class<R> outputType;
//
//    public BaseLLMCapabilityUnit(Class<T> inputType, Class<R> outputType) {
//        this.inputType = inputType;
//        this.outputType = outputType;
//    }
//
//    public Class<T> getInputType() {
//        return inputType;
//    }
//
//    public Class<R> getOutputType() {
//        return outputType;
//    }
//
//    public abstract R run(T input);
//}
//
