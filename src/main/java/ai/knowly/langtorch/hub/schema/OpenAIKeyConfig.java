package ai.knowly.langtorch.hub.schema;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAIKeyConfig {
  private String openAiApiKey;
  // Read the OpenAI API key from the .env file.
  // If set, no need to set the openAiApiKey explicitly.
  private boolean readFromEnvFile;

  public Optional<String> getOpenAiApiKey() {
    return Optional.ofNullable(openAiApiKey);
  }

  public static OpenAIKeyConfig createOpenConfigReadFromEnv() {
    return new OpenAIKeyConfig(null, true);
  }

  public static OpenAIKeyConfig createOpenConfigWithApiKey(String apiKey) {
    return new OpenAIKeyConfig(apiKey, false);
  }
}
