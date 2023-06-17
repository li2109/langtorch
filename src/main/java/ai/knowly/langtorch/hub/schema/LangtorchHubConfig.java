package ai.knowly.langtorch.hub.schema;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LangtorchHubConfig {
  private OpenAIKeyConfig openAIKeyConfig;

  public Optional<OpenAIKeyConfig> getOpenAIKeyConfig() {
    return Optional.ofNullable(openAIKeyConfig);
  }
}
