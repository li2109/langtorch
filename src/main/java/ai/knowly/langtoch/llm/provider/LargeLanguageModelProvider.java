package ai.knowly.langtoch.llm.provider;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.ProcessorType;
import ai.knowly.langtoch.llm.schema.io.Input;
import ai.knowly.langtoch.llm.schema.io.Output;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for Large Language Model providers. Defines the common interface and methods
 * for handling multiple processor types.
 */
public abstract class LargeLanguageModelProvider {

  // Map to store supported processor types and their corresponding processor instances
  protected Map<ProcessorType, Processor<? extends Input, ? extends Output>> supportedProcessors =
      new HashMap<>();

  // Abstract method for validating the processor type and instance combination
  protected abstract void validateType(
      ProcessorType processorType, Processor<? extends Input, ? extends Output> processor);

  // Method to add a processor instance for a specific processor type
  public LargeLanguageModelProvider withProcessor(
      ProcessorType processorType, Processor<? extends Input, ? extends Output> processor) {
    this.supportedProcessors.put(processorType, processor);
    return this;
  }

  // Method to run a given input using a specific processor type and return the output
  public <I extends Input, O extends Output> O run(I inputData, ProcessorType processorType) {
    // Validate that the processor type is supported
    Processor<I, O> processor = (Processor<I, O>) supportedProcessors.get(processorType);
    validateType(processorType, processor);

    return processor.run(inputData);
  }
}
