package ai.knowly.langtorch.llm.openai.schema.config;

import ai.knowly.langtorch.utils.future.retry.RetryConfig;
import ai.knowly.langtorch.utils.future.retry.strategy.BackoffStrategy;
import ai.knowly.langtorch.utils.future.retry.strategy.ExponentialBackoffStrategy;
import com.google.auto.value.AutoValue;
import java.time.Duration;
import java.util.Optional;

/**
 * The OpenAIServiceConfig class is an AutoValue class with a builder pattern that contains various
 * configurations for an OpenAI service.
 */
@AutoValue
public abstract class OpenAIServiceConfig {
  public static Builder builder() {
    return new AutoValue_OpenAIServiceConfig.Builder()
        .setTimeoutDuration(Duration.ofSeconds(10))
        .setRetryConfig(RetryConfig.getDefaultInstance())
        .setBackoffStrategy(new ExponentialBackoffStrategy());
  }

  public abstract String apiKey();

  public abstract Duration timeoutDuration();

  public abstract Optional<OpenAIProxyConfig> proxyConfig();

  public abstract BackoffStrategy backoffStrategy();

  public abstract RetryConfig retryConfig();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setApiKey(String newApiKey);

    public abstract Builder setTimeoutDuration(Duration newTimeoutDuration);

    public abstract Builder setProxyConfig(OpenAIProxyConfig newProxyConfig);

    public abstract Builder setBackoffStrategy(BackoffStrategy newBackoffStrategy);

    public abstract Builder setRetryConfig(RetryConfig newRetryConfig);

    public abstract OpenAIServiceConfig build();
  }
}
