package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.Input;
import ai.knowly.langtoch.llm.schema.io.Output;

public abstract class CapabilityUnit<I extends Input, R extends Output> {
  protected Processor<I, R> processor;

  protected CapabilityUnit(Processor<I, R> processor) {
    this.processor = processor;
  }

  public R run(I input) {
    return processor.run(input);
  }
}
