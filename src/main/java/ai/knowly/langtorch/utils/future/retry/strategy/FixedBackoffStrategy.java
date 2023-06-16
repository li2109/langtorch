package ai.knowly.langtorch.utils.future.retry.strategy;

public class FixedBackoffStrategy implements BackoffStrategy {
  @Override
  public long getDelayMillis(int retryCount, long intervalMillis) {
    return intervalMillis;
  }
}
