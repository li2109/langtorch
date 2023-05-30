package ai.knowly.langtorch.capability.modality.text;

import ai.knowly.langtorch.capability.Capability;
import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;

/** Interface for a text language model capability. */
public interface TextLLMCapability<T, I extends Input, O extends Output, R>
    extends Capability<T, R> {
  I preProcess(T inputData);

  R postProcess(O outputData);
}
