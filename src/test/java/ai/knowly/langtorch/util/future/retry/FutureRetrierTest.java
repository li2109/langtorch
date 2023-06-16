package ai.knowly.langtorch.util.future.retry;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

import ai.knowly.langtorch.utils.future.retry.FutureRetrier;
import ai.knowly.langtorch.utils.future.retry.strategy.BackoffStrategy;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.*;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FutureRetrierTest {
  private ScheduledExecutorService executor;
  @Mock private BackoffStrategy backoffStrategy;
  private FutureRetrier futureRetrier;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    executor = Executors.newScheduledThreadPool(1);
    futureRetrier = new FutureRetrier(executor, backoffStrategy);
  }

  @AfterEach
  void teardown() {
    executor.shutdownNow();
  }

  @Test
  void runWithRetries_shouldExecuteSupplierAndCheckSuccessCondition() throws Exception {
    // Arrange.
    Supplier<ListenableFuture<String>> futureSupplier = () -> Futures.immediateFuture("success");
    Predicate<String> successCondition = "success"::equals;

    // Act.
    ListenableFuture<String> future =
        futureRetrier.runWithRetries(futureSupplier, 3, 1000, successCondition);

    // Assert.
    assertThat(future.isDone()).isTrue();
    assertThat(future.get()).isEqualTo("success");
  }

  @Test
  void handleFailure_shouldRetry_whenRetriesAreNotExhausted() throws Exception {
    // Arrange.
    AtomicInteger attempts = new AtomicInteger(0);
    Supplier<ListenableFuture<String>> futureSupplier =
        () -> {
          if (attempts.incrementAndGet() < 3) {
            return Futures.immediateFailedFuture(new Exception("failure"));
          } else {
            return Futures.immediateFuture("success");
          }
        };
    Predicate<String> successCondition = "success"::equals;
    when(backoffStrategy.getDelayMillis(anyInt(), anyLong())).thenReturn(1000L);

    // Act.
    ListenableFuture<String> future =
        futureRetrier.runWithRetries(futureSupplier, 3, 1000, successCondition);

    // Allow enough time for the retries to complete
    Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);

    // Assert.
    assertThat(future.isDone()).isTrue();
    assertThat(future.get()).isEqualTo("success");
  }

  @Test
  void handleFailure_shouldSetException_whenRetriesAreExhausted() {
    // Arrange.
    Supplier<ListenableFuture<String>> futureSupplier =
        () -> {
          throw new RuntimeException("Error");
        };
    Predicate<String> successCondition = "success"::equals;

    // Act.
    Throwable t = null;
    try {
      ListenableFuture<String> future =
          futureRetrier.runWithRetries(futureSupplier, 1, 1000, successCondition);
      future.get();
    } catch (ExecutionException | InterruptedException ex) {
      t = ex;
    }

    // Assert.
    assertThat(t).isInstanceOf(ExecutionException.class);
    assertThat(t.getCause()).isInstanceOf(RuntimeException.class);
    assertThat(t.getCause().getMessage()).isEqualTo("Error");
  }
}
