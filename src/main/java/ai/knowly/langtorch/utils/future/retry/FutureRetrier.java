package ai.knowly.langtorch.utils.future.retry;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import ai.knowly.langtorch.utils.future.retry.strategy.BackoffStrategy;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/** A utility class for retrying a {@link ListenableFuture} until a success condition is met. */
public final class FutureRetrier {
  static int RUN_FOREVER = Integer.MIN_VALUE;

  private final ScheduledExecutorService executor;
  private final BackoffStrategy backoffStrategy;
  private final RetryConfig retryConfig;

  @Inject
  public FutureRetrier(
      ScheduledExecutorService executor, BackoffStrategy backoffStrategy, RetryConfig retryConfig) {
    this.executor = executor;
    this.backoffStrategy = backoffStrategy;
    this.retryConfig = retryConfig;
  }

  public <T> ListenableFuture<T> runWithRetries(
      Supplier<ListenableFuture<T>> futureSupplier, Predicate<T> successCondition) {
    return runWithRetries(
        futureSupplier,
        retryConfig.getMaxRetries(),
        retryConfig.getRetryIntervalMillis(),
        successCondition);
  }

  public <T> ListenableFuture<T> runWithRetries(
      Supplier<ListenableFuture<T>> futureSupplier,
      int retries,
      long intervalMillis,
      Predicate<T> successCondition) {
    SettableFuture<T> resultFuture = SettableFuture.create();
    runWithRetriesInternal(
        resultFuture, futureSupplier, retries, intervalMillis, successCondition, 0);
    return resultFuture;
  }

  private <T> void runWithRetriesInternal(
      final SettableFuture<T> future,
      final Supplier<ListenableFuture<T>> futureSupplier,
      final int retries,
      final long intervalMillis,
      final Predicate<T> successCondition,
      final int retryCount) {
    ListenableFuture<T> immediateFuture;
    try {
      immediateFuture = futureSupplier.get();
    } catch (Exception e) {
      handleFailure(
          future, futureSupplier, retries, intervalMillis, successCondition, e, retryCount);
      return;
    }

    Futures.addCallback(
        immediateFuture,
        new FutureCallback<T>() {
          @Override
          public void onSuccess(T result) {
            if (successCondition.apply(result)) {
              future.set(result);
            } else {
              RuntimeException exception =
                  new RuntimeException("Success condition not met, retrying.");
              handleFailure(
                  future,
                  futureSupplier,
                  retries,
                  intervalMillis,
                  successCondition,
                  exception,
                  retryCount + 1);
            }
          }

          @Override
          public void onFailure(Throwable t) {
            handleFailure(
                future,
                futureSupplier,
                retries,
                intervalMillis,
                successCondition,
                t,
                retryCount + 1);
          }
        },
        MoreExecutors.directExecutor());
  }

  private <T> void handleFailure(
      SettableFuture<T> future,
      Supplier<ListenableFuture<T>> futureSupplier,
      int retries,
      long delayInMillis,
      Predicate<T> successCondition,
      Throwable t,
      int retryCount) {
    if (retries == RUN_FOREVER || retries > 0) {
      ScheduledFuture<?> unused =
          executor.schedule(
              () -> {
                int newRetriesCount = retries == RUN_FOREVER ? RUN_FOREVER : retries - 1;
                runWithRetriesInternal(
                    future,
                    futureSupplier,
                    newRetriesCount,
                    delayInMillis,
                    successCondition,
                    retryCount + 1);
              },
              backoffStrategy.getDelayMillis(retryCount, delayInMillis),
              MILLISECONDS);
    } else {
      future.setException(t);
    }
  }
}
