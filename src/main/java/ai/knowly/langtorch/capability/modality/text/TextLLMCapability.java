package ai.knowly.langtorch.capability.modality.text;

import ai.knowly.langtorch.capability.Capability;
import ai.knowly.langtorch.memory.Memory;
import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;
import ai.knowly.langtorch.schema.memory.MemoryKey;
import ai.knowly.langtorch.schema.memory.MemoryValue;
import java.util.Optional;

public interface TextLLMCapability<T, I extends Input, O extends Output, R>
    extends Capability<T, R> {
  I preProcess(T inputData);

  Optional<Memory<MemoryKey, MemoryValue>> getMemory();

  R postProcess(O outputData);
}
