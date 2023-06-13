package ai.knowly.langtorch.processor.openai.image;

import ai.knowly.langtorch.processor.ProcessorConfig;
import com.google.auto.value.AutoValue;
import java.util.Optional;

@AutoValue
public abstract class OpenAIImageProcessorConfig implements ProcessorConfig {
  public static OpenAIImageProcessorConfig.Builder builder() {
    return new AutoValue_OpenAIImageProcessorConfig.Builder();
  }

  // Method to create a builder from the current instance
  abstract OpenAIImageProcessorConfig.Builder toBuilder();

  public abstract Optional<Integer> getN();

  // The size of the generated images. Must be one of "256x256", "512x512", or "1024x1024".
  // Defaults to "1024x1024"
  public abstract Optional<String> getSize();

  public abstract Optional<String> getUser();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract OpenAIImageProcessorConfig.Builder setSize(String size);

    public abstract OpenAIImageProcessorConfig.Builder setN(Integer n);

    public abstract OpenAIImageProcessorConfig.Builder setUser(String user);

    public abstract OpenAIImageProcessorConfig build();
  }
}
