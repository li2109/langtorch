package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.input.Input;
import ai.knowly.langtoch.llm.schema.io.output.Output;

public abstract class BaseCapabilityUnit<I extends Input, R extends Output> {
  protected Processor<I, R> processor;

  protected BaseCapabilityUnit(Processor<I, R> processor) {
    this.processor = processor;
  }

  public R run(I input) {
    return processor.run(input);
  }
}
