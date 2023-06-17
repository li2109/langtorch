package ai.knowly.langtorch.llm.minimax.schema.config;

import ai.knowly.langtorch.utils.future.retry.RetryConfig;
import ai.knowly.langtorch.utils.future.retry.strategy.BackoffStrategy;
import ai.knowly.langtorch.utils.future.retry.strategy.ExponentialBackoffStrategy;
import com.google.auto.value.AutoValue;
import java.time.Duration;

/**
 * @author maxiao
 * @date 2023/06/07
 */
@AutoValue
public abstract class MiniMaxServiceConfig {
  public static Builder builder() {
    return new AutoValue_MiniMaxServiceConfig.Builder()
        .setTimeoutDuration(Duration.ofSeconds(10))
        .setRetryConfig(RetryConfig.getDefaultInstance())
        .setBackoffStrategy(new ExponentialBackoffStrategy());
  }

  public abstract String groupId();

  public abstract String apiKey();

  public abstract Duration timeoutDuration();

  public abstract BackoffStrategy backoffStrategy();

  public abstract RetryConfig retryConfig();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setGroupId(String newGroupId);

    public abstract Builder setApiKey(String newApiKey);

    public abstract Builder setTimeoutDuration(Duration newTimeoutDuration);

    public abstract Builder setBackoffStrategy(BackoffStrategy newBackoffStrategy);

    public abstract Builder setRetryConfig(RetryConfig newRetryConfig);

    public abstract MiniMaxServiceConfig build();
  }
}
