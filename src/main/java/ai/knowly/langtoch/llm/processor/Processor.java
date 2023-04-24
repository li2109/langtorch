package ai.knowly.langtoch.llm.processor;

import ai.knowly.langtoch.llm.schema.io.Input;
import ai.knowly.langtoch.llm.schema.io.Output;

/**
 * Processor is LLM model's capability of taking/generating data of different modalities or types.
 */
public interface Processor<I extends Input, O extends Output> {
  O run(I inputData);
}
