package ai.knowly.langtorch.processor.module.openai.embeddings;

import ai.knowly.langtorch.processor.module.ProcessorConfig;
import com.google.auto.value.AutoValue;
import java.util.Optional;

@AutoValue
public abstract class OpenAIEmbeddingsProcessorConfig implements ProcessorConfig {

  public static OpenAIEmbeddingsProcessorConfig.Builder builder() {
    return new AutoValue_OpenAIEmbeddingsProcessorConfig.Builder();
  }

  public abstract Optional<String> getUser();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract OpenAIEmbeddingsProcessorConfig.Builder setUser(String user);

    public abstract OpenAIEmbeddingsProcessorConfig build();
  }
}
