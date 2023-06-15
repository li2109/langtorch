package ai.knowly.langtorch.processor.module.minimax.embeddings;

import ai.knowly.langtorch.processor.module.ProcessorConfig;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MiniMaxEmbeddingsProcessorConfig implements ProcessorConfig {

  public static MiniMaxEmbeddingsProcessorConfig.Builder builder() {
    return new AutoValue_MiniMaxEmbeddingsProcessorConfig.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract MiniMaxEmbeddingsProcessorConfig build();
  }
}
