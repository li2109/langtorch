package ai.knowly.langtorch.utils.future.retry.strategy;

public class FibonacciBackoffStrategy implements BackoffStrategy {
  @Override
  public long getDelayMillis(int retryCount, long intervalMillis) {
    return intervalMillis * fibonacci(retryCount);
  }

  private long fibonacci(int n) {
    if (n <= 1) {
      return n;
    }
    long fib = 1;
    long prevFib = 1;

    for (int i = 2; i < n; i++) {
      long temp = fib;
      fib += prevFib;
      prevFib = temp;
    }
    return fib;
  }
}
