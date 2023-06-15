package ai.knowly.langtorch.hub.schema;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAIKeyConfig {
  private String openAiApiKey;
  // Read the OpenAI API key from the .env file.
  // If set, no need to set the openAiApiKey explicitly.
  private boolean readFromEnvFile;

  public Optional<String> getOpenAiApiKey() {
    return Optional.ofNullable(openAiApiKey);
  }
}
