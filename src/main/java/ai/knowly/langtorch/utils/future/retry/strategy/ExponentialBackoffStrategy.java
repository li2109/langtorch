package ai.knowly.langtorch.utils.future.retry.strategy;

public class ExponentialBackoffStrategy implements BackoffStrategy {
  @Override
  public long getDelayMillis(int retryCount, long intervalMillis) {
    return (long) (intervalMillis * Math.pow(2, retryCount));
  }
}
