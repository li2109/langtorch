package ai.knowly.langtorch.agent;

import com.google.common.util.concurrent.ListenableFuture;

/** Interface for tool. */
public interface Tool<T, R> {
  R run(T inputData);

  ListenableFuture<R> runAsync(T inputData);
}
