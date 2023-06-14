package ai.knowly.langtorch.processor;

import ai.knowly.langtorch.schema.io.Input;
import ai.knowly.langtorch.schema.io.Output;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Processor is LLM model's capability of taking/generating data of different modalities or types.
 */
public interface Processor<I extends Input, O extends Output> {
  O run(I inputData);

  ListenableFuture<O> runAsync(I inputData);
}
