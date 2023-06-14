package ai.knowly.langtorch.processor.module.openai.chat;

import ai.knowly.langtorch.processor.module.ProcessorConfig;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Configuration class for OpenAIChatProcessor with various options
@AutoValue
public abstract class OpenAIChatProcessorConfig implements ProcessorConfig {
  private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
  private static final int DEFAULT_MAX_TOKEN = 2048;

  public static OpenAIChatProcessorConfig getDefaultInstance() {
    return builder().build();
  }

  public static Builder builder() {
    return new AutoValue_OpenAIChatProcessorConfig.Builder()
        .setModel(DEFAULT_MODEL)
        .setMaxTokens(DEFAULT_MAX_TOKEN)
        .setStop(new ArrayList<>())
        .setLogitBias(new HashMap<>());
  }

  // Method to create a builder from the current instance
  public abstract Builder toBuilder();

  // Abstract methods for configuration properties
  public abstract String getModel();

  public abstract Optional<Double> getTemperature();

  public abstract Optional<Double> getTopP();

  public abstract Optional<Integer> getN();

  public abstract Optional<Boolean> getStream();

  public abstract ImmutableList<String> getStop();

  public abstract Optional<Integer> getMaxTokens();

  public abstract Optional<Double> getPresencePenalty();

  public abstract Optional<Double> getFrequencyPenalty();

  public abstract ImmutableMap<String, Integer> getLogitBias();

  public abstract Optional<String> getUser();

  // Builder class for constructing OpenAIChatProcessorConfig instances
  @AutoValue.Builder
  public abstract static class Builder {
    // Builder methods for setting configuration properties
    public abstract Builder setModel(String model);

    public abstract Builder setTemperature(Double temperature);

    public abstract Builder setTopP(Double topP);

    public abstract Builder setN(Integer n);

    public abstract Builder setStream(Boolean stream);

    public abstract Builder setStop(List<String> stop);

    public abstract Builder setMaxTokens(Integer maxTokens);

    public abstract Builder setPresencePenalty(Double presencePenalty);

    public abstract Builder setFrequencyPenalty(Double frequencyPenalty);

    public abstract Builder setLogitBias(Map<String, Integer> logitBias);

    public abstract Builder setUser(String user);

    // Method to build an instance of OpenAIChatProcessorConfig
    public abstract OpenAIChatProcessorConfig build();
  }
}
