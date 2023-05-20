package ai.knowly.langtoch.llm.integration.openai.service.schema.config;

import com.google.auto.value.AutoValue;
import lombok.Builder;

@AutoValue
public abstract class OpenAIProxyConfig {
  public static Builder builder() {
    return new AutoValue_OpenAIProxyConfig.Builder();
  }

  public abstract ProxyType proxyType();

  public abstract String proxyHost();

  public abstract Integer proxyPort();

  public enum ProxyType {
    HTTP,
    SOCKS
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setProxyType(ProxyType newProxyType);

    public abstract Builder setProxyHost(String newProxyHost);

    public abstract Builder setProxyPort(int newProxyPort);

    public abstract OpenAIProxyConfig build();
  }
}
