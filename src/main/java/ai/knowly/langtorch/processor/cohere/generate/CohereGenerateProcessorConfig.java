package ai.knowly.langtorch.processor.cohere.generate;

import ai.knowly.langtorch.processor.ProcessorConfig;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Optional;

@AutoValue
public abstract class CohereGenerateProcessorConfig implements ProcessorConfig {
  public static Builder builder() {
    return new AutoValue_CohereGenerateProcessorConfig.Builder()
        .setModel("command")
        .setEndSequences(ImmutableList.of())
        .setStopSequences(ImmutableList.of())
        .setLogitBias(ImmutableMap.of());
  }

  public abstract Builder toBuilder();

  // Abstract methods for configuration properties
  public abstract String getModel();

  public abstract Optional<String> getPresent();

  public abstract Optional<Double> getTemperature();

  public abstract Optional<Double> getP();

  public abstract Optional<Integer> getK();

  public abstract Optional<Integer> getMaxTokens();

  public abstract Optional<Integer> getNumGenerations();

  public abstract Optional<Double> getPresencePenalty();

  public abstract Optional<Double> getFrequencyPenalty();

  public abstract ImmutableMap<String, Float> getLogitBias();

  public abstract List<String> getEndSequences();

  public abstract List<String> getStopSequences();

  public abstract Optional<CohereGenerateReturnLikelihoods> getReturnLikelihoods();

  public abstract Optional<CohereGenerateTruncate> getTruncate();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setModel(String newModel);

    public abstract Builder setPresent(String newPresent);

    public abstract Builder setTemperature(double newTemperature);

    public abstract Builder setP(double newP);

    public abstract Builder setK(int newK);

    public abstract Builder setMaxTokens(int newMaxTokens);

    public abstract Builder setNumGenerations(int newNumGenerations);

    public abstract Builder setPresencePenalty(double newPresencePenalty);

    public abstract Builder setFrequencyPenalty(double newFrequencyPenalty);

    public abstract Builder setLogitBias(ImmutableMap<String, Float> newLogitBias);

    public abstract Builder setEndSequences(List<String> newEndSequences);

    public abstract Builder setStopSequences(List<String> newStopSequences);

    public abstract Builder setReturnLikelihoods(
        CohereGenerateReturnLikelihoods newReturnLikelihoods);

    public abstract Builder setTruncate(CohereGenerateTruncate newTruncate);

    public abstract CohereGenerateProcessorConfig build();
  }
}
