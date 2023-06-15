package ai.knowly.langtorch.hub;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LangtorchHubConfig {
  private String openAiApiKey;

  public Optional<String> getOpenAiApiKey() {
    return Optional.ofNullable(openAiApiKey);
  }
}
