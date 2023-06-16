package ai.knowly.langtorch.utils.future.retry.strategy;

public interface BackoffStrategy {
  long getDelayMillis(int retryCount, long intervalMillis);
}
