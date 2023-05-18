package ai.knowly.langtoch.llm.processor;

import ai.knowly.langtoch.schema.io.Input;
import ai.knowly.langtoch.schema.io.Output;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Processor is LLM model's capability of taking/generating data of different modalities or types.
 */
public interface Processor<I extends Input, O extends Output> {
  O run(I inputData);

  ListenableFuture<O> runAsync(I inputData);
}
