package ai.knowly.langtoch.capability;

import java.util.concurrent.CompletableFuture;

public interface Capability<T, R> {
  R run(T inputData);

  CompletableFuture<R> runAsync(CompletableFuture<T> inputData);
}
