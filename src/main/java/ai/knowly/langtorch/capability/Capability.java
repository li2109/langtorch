package ai.knowly.langtorch.capability;

import com.google.common.util.concurrent.ListenableFuture;

/** Interface for a capability. */
public interface Capability<T, R> {
  R run(T inputData);

  ListenableFuture<R> runAsync(T inputData);
}
