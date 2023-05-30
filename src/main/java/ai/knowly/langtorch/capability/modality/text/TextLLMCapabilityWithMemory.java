package ai.knowly.langtorch.capability.modality.text;

import ai.knowly.langtorch.capability.Capability;
import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;
import ai.knowly.langtorch.store.memory.Memory;
import ai.knowly.langtorch.store.memory.MemoryContext;
import ai.knowly.langtorch.store.memory.MemoryValue;
import java.util.Optional;

/** Interface for a text language model capability with memory. */
public interface TextLLMCapabilityWithMemory<
        T, I extends Input, O extends Output, R, V extends MemoryValue, S extends MemoryContext>
    extends Capability<T, R> {
  I preProcess(T inputData);

  Optional<Memory<V, S>> getMemory();

  R postProcess(O outputData);
}
