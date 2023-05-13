package ai.knowly.langtoch.capability.unit;

import java.util.concurrent.CompletableFuture;

public interface CapabilityUnit<T, R> {
  R run(T inputData);

  CompletableFuture<R> runAsync(CompletableFuture<T> inputData);
}
