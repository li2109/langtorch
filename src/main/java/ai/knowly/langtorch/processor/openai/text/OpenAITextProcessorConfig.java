package ai.knowly.langtorch.processor.openai.text;

import ai.knowly.langtorch.processor.ProcessorConfig;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Configuration class for OpenAITextProcessor with various options
@AutoValue
public abstract class OpenAITextProcessorConfig implements ProcessorConfig {
  @VisibleForTesting static final String DEFAULT_MODEL = "text-davinci-003";
  private static final int DEFAULT_MAX_TOKENS = 2048;

  public static OpenAITextProcessorConfig getDefaultInstance() {
    return builder().build();
  }

  public static Builder builder() {
    return new AutoValue_OpenAITextProcessorConfig.Builder()
        .setModel(DEFAULT_MODEL)
        .setMaxTokens(DEFAULT_MAX_TOKENS)
        .setLogitBias(new HashMap<>())
        .setStop(new ArrayList<>());
  }

  // Method to create a builder from the current instance
  abstract Builder toBuilder();

  // Abstract methods for configuration properties
  public abstract String getModel();

  public abstract Optional<String> getSuffix();

  public abstract Optional<Integer> getMaxTokens();

  public abstract Optional<Double> getTemperature();

  public abstract Optional<Double> getTopP();

  public abstract Optional<Integer> getN();

  public abstract Optional<Boolean> getStream();

  public abstract Optional<Integer> getLogprobs();

  public abstract Optional<Boolean> getEcho();

  public abstract ImmutableList<String> getStop();

  public abstract Optional<Double> getPresencePenalty();

  public abstract Optional<Double> getFrequencyPenalty();

  public abstract Optional<Integer> getBestOf();

  public abstract ImmutableMap<String, Integer> getLogitBias();

  public abstract Optional<String> getUser();

  // Builder class for constructing OpenAITextProcessorConfig instances
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setModel(String model);

    public abstract Builder setSuffix(String suffix);

    public abstract Builder setMaxTokens(Integer maxTokens);

    public abstract Builder setTemperature(Double temperature);

    public abstract Builder setTopP(Double topP);

    public abstract Builder setN(Integer n);

    public abstract Builder setStream(Boolean stream);

    public abstract Builder setLogprobs(Integer logprobs);

    public abstract Builder setEcho(Boolean echo);

    public abstract Builder setStop(List<String> stop);

    public abstract Builder setPresencePenalty(Double presencePenalty);

    public abstract Builder setFrequencyPenalty(Double frequencyPenalty);

    public abstract Builder setBestOf(Integer bestOf);

    public abstract Builder setLogitBias(Map<String, Integer> logitBias);

    public abstract Builder setUser(String user);

    // Method to build an instance of OpenAITextProcessorConfig
    public abstract OpenAITextProcessorConfig build();
  }
}
