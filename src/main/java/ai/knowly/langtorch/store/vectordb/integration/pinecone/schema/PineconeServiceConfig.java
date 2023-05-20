package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema;

import com.google.auto.value.AutoValue;
import java.time.Duration;

@AutoValue
public abstract class PineconeServiceConfig {
  public static Builder builder() {
    return new AutoValue_PineconeServiceConfig.Builder()
        .setTimeoutDuration(Duration.ofSeconds(10))
        .setEnableLogging(false);
  }

  public abstract String apiKey();

  public abstract String endpoint();

  public abstract Duration timeoutDuration();

  public abstract boolean enableLogging();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setEndpoint(String endpoint);

    public abstract Builder setApiKey(String newApiKey);

    public abstract Builder setTimeoutDuration(Duration timeoutDuration);

    public abstract Builder setEnableLogging(boolean enableLogging);

    public abstract PineconeServiceConfig build();
  }
}
