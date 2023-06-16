package ai.knowly.langtorch.util.future.retry.strategy;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.utils.future.retry.strategy.BackoffStrategy;
import ai.knowly.langtorch.utils.future.retry.strategy.ExponentialBackoffStrategy;
import ai.knowly.langtorch.utils.future.retry.strategy.FibonacciBackoffStrategy;
import ai.knowly.langtorch.utils.future.retry.strategy.FixedBackoffStrategy;
import org.junit.jupiter.api.Test;

class BackoffStrategyTest {
  @Test
  void testFixedBackoffStrategy() {
    // Arrange
    BackoffStrategy strategy = new FixedBackoffStrategy();
    long intervalMillis = 1000;

    // Act
    long delayMillis = strategy.getDelayMillis(3, intervalMillis);

    // Assert
    assertThat(delayMillis).isEqualTo(intervalMillis);
  }

  @Test
  void testExponentialBackoffStrategy() {
    // Arrange
    BackoffStrategy strategy = new ExponentialBackoffStrategy();
    long intervalMillis = 1000;

    // Act
    long delayMillis = strategy.getDelayMillis(3, intervalMillis);

    // Assert
    // 2^3 = 8
    assertThat(delayMillis).isEqualTo(intervalMillis * 8);
  }

  @Test
  void testFibonacciBackoffStrategy() {
    // Arrange
    BackoffStrategy strategy = new FibonacciBackoffStrategy();
    long intervalMillis = 1000;

    // Act
    long delayMillis = strategy.getDelayMillis(5, intervalMillis);

    // Assert
    // Fibonacci sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34...
    // At position 5: 5
    assertThat(delayMillis).isEqualTo(intervalMillis * 5);
  }
}
