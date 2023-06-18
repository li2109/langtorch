package ai.knowly.langtorch.processor.minimax.chat;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.processor.ProcessorConfig;
import com.google.auto.value.AutoValue;
import java.util.Optional;

/**
 * Configuration class for MiniMaxChatProcessor with various options
 *
 * @author maxiao
 * @date 2023/06/08
 */
@AutoValue
public abstract class MiniMaxChatProcessorConfig implements ProcessorConfig {
  private static final String DEFAULT_MODEL = "abab5-chat";

  public static MiniMaxChatProcessorConfig getDefaultInstance() {
    return builder().build();
  }

  public static Builder builder() {
    return new AutoValue_MiniMaxChatProcessorConfig.Builder().setModel(DEFAULT_MODEL);
  }

  // Method to create a builder from the current instance
  public abstract Builder toBuilder();

  // Abstract methods for configuration properties
  public abstract String getModel();

  public abstract Optional<Boolean> getWithEmotion();

  public abstract Optional<Boolean> getStream();

  public abstract Optional<Boolean> getUseStandardSse();

  public abstract Optional<Integer> getBeamWidth();

  public abstract Optional<String> getPrompt();

  public abstract Optional<ChatCompletionRequest.RoleMeta> getRoleMeta();

  public abstract Optional<Boolean> getContinueLastMessage();

  public abstract Optional<Long> getTokensToGenerate();

  public abstract Optional<Float> getTemperature();

  public abstract Optional<Float> getTopP();

  public abstract Optional<Boolean> getSkipInfoMask();

  // Builder class for constructing MiniMaxChatProcessorConfig instances
  @AutoValue.Builder
  public abstract static class Builder {
    // Builder methods for setting configuration properties
    public abstract Builder setModel(String model);

    public abstract Builder setWithEmotion(Boolean withEmotion);

    public abstract Builder setStream(Boolean stream);

    public abstract Builder setUseStandardSse(Boolean useStandardSse);

    public abstract Builder setBeamWidth(Integer beamWidth);

    public abstract Builder setPrompt(String prompt);

    public abstract Builder setRoleMeta(ChatCompletionRequest.RoleMeta roleMeta);

    public abstract Builder setContinueLastMessage(Boolean continueLastMessage);

    public abstract Builder setTokensToGenerate(Long tokensToGenerate);

    public abstract Builder setTemperature(Float temperature);

    public abstract Builder setTopP(Float topP);

    public abstract Builder setSkipInfoMask(Boolean skipInfoMask);

    // Method to build an instance of MiniMaxChatProcessorConfig
    public abstract MiniMaxChatProcessorConfig build();
  }
}
