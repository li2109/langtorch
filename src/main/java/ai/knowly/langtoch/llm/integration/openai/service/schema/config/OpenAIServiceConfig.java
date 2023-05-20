package ai.knowly.langtoch.llm.integration.openai.service.schema.config;

import com.google.auto.value.AutoValue;
import java.time.Duration;
import java.util.Optional;
import lombok.Builder;

@AutoValue
public abstract class OpenAIServiceConfig {
  public static Builder builder() {
    return new AutoValue_OpenAIServiceConfig.Builder().setTimeoutDuration(Duration.ofSeconds(10));
  }

  public abstract String apiKey();

  public abstract Duration timeoutDuration();

  public abstract Optional<OpenAIProxyConfig> proxyConfig();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setApiKey(String newApiKey);

    public abstract Builder setTimeoutDuration(Duration newTimeoutDuration);

    public abstract Builder setProxyConfig(OpenAIProxyConfig newProxyConfig);

    public abstract OpenAIServiceConfig build();
  }
}
