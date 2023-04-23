package ai.knowly.langtoch.llm.processor.openai.text;

import ai.knowly.langtoch.llm.schema.io.input.Input;
import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoValue
public abstract class TextProcessorConfig implements Input {
  private static final String DEFAULT_MODEL = "text-davinci-003";
  private static final int DEFAULT_MAX_TOKENS = 2048;

  public static Builder builder() {
    return new AutoValue_TextProcessorConfig.Builder()
        .setModel(DEFAULT_MODEL)
        .setMaxTokens(DEFAULT_MAX_TOKENS)
        .setLogitBias(Map.of())
        .setStop(List.of());
  }

  abstract Builder toBuilder();

  public abstract String getModel();

  public abstract Optional<String> getSuffix();

  public abstract Optional<Integer> getMaxTokens();

  public abstract Optional<Double> getTemperature();

  public abstract Optional<Double> getTopP();

  public abstract Optional<Integer> getN();

  public abstract Optional<Boolean> getStream();

  public abstract Optional<Integer> getLogprobs();

  public abstract Optional<Boolean> getEcho();

  public abstract List<String> getStop();

  public abstract Optional<Double> getPresencePenalty();

  public abstract Optional<Double> getFrequencyPenalty();

  public abstract Optional<Integer> getBestOf();

  public abstract Map<String, Integer> getLogitBias();

  public abstract Optional<String> getUser();

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

    public abstract TextProcessorConfig build();
  }
}
