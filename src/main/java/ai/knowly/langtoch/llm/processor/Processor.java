package ai.knowly.langtoch.llm.processor;

import ai.knowly.langtoch.schema.io.Input;
import ai.knowly.langtoch.schema.io.Output;
import java.util.concurrent.CompletableFuture;

/**
 * Processor is LLM model's capability of taking/generating data of different modalities or types.
 */
public interface Processor<I extends Input, O extends Output> {
  O run(I inputData);

  CompletableFuture<O> runAsync(CompletableFuture<I> inputData);
}
