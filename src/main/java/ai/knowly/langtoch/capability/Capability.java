package ai.knowly.langtoch.capability;

import com.google.common.util.concurrent.ListenableFuture;

public interface Capability<T, R> {
  R run(T inputData);

  ListenableFuture<R> runAsync(T inputData);
}
