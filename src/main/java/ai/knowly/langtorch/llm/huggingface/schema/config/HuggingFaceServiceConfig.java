package ai.knowly.langtorch.llm.huggingface.schema.config;

import ai.knowly.langtorch.utils.future.retry.RetryConfig;
import ai.knowly.langtorch.utils.future.retry.strategy.BackoffStrategy;
import ai.knowly.langtorch.utils.future.retry.strategy.ExponentialBackoffStrategy;
import com.google.auto.value.AutoValue;
import java.time.Duration;

/**
 * The HuggingFaceServiceConfig class is an AutoValue class with a builder pattern that contains
 * various configurations for HuggingFace service.
 */
@AutoValue
public abstract class HuggingFaceServiceConfig {
  public static Builder builder() {
    return new AutoValue_HuggingFaceServiceConfig.Builder()
        .setTimeoutDuration(Duration.ofSeconds(10))
        .setRetryConfig(RetryConfig.getDefaultInstance())
        .setBackoffStrategy(new ExponentialBackoffStrategy());
  }

  public abstract String apiToken();

  public abstract String modelId();

  public abstract Duration timeoutDuration();

  public abstract BackoffStrategy backoffStrategy();

  public abstract RetryConfig retryConfig();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setApiToken(String newApiKey);

    public abstract Builder setModelId(String newModelId);

    public abstract Builder setTimeoutDuration(Duration newTimeoutDuration);

    public abstract Builder setBackoffStrategy(BackoffStrategy newBackoffStrategy);

    public abstract Builder setRetryConfig(RetryConfig newRetryConfig);

    public abstract HuggingFaceServiceConfig build();
  }
}
