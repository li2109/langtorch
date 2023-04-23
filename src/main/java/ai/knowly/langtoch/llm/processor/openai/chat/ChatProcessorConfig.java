package ai.knowly.langtoch.llm.processor.openai.chat;

import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoValue
public abstract class ChatProcessorConfig {
  private static final String DEFAULT_MODEL = "gpt-3.5-turbo";

  public static Builder builder() {
    return new AutoValue_ChatProcessorConfig.Builder()
        .setModel(DEFAULT_MODEL)
        .setStop(List.of())
        .setLogitBias(Map.of());
  }

  public abstract Builder toBuilder();

  public abstract String getModel();

  public abstract Optional<Double> getTemperature();

  public abstract Optional<Double> getTopP();

  public abstract Optional<Integer> getN();

  public abstract Optional<Boolean> getStream();

  public abstract List<String> getStop();

  public abstract Optional<Integer> getMaxTokens();

  public abstract Optional<Double> getPresencePenalty();

  public abstract Optional<Double> getFrequencyPenalty();

  public abstract Map<String, Integer> getLogitBias();

  public abstract Optional<String> getUser();

  @AutoValue.Builder
  public abstract static class Builder {
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

    public abstract ChatProcessorConfig build();
  }
}
