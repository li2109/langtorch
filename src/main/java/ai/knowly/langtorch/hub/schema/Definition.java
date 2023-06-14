package ai.knowly.langtorch.hub.schema;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Definition {
  private TorchletDefinition torchletDefinition;
  private TorchletProviderDefinition torchletProviderDefinition;

  public Optional<TorchletDefinition> getTorchletDefinition() {
    return Optional.ofNullable(torchletDefinition);
  }

  public Optional<TorchletProviderDefinition> getTorchletProviderDefinition() {
    return Optional.ofNullable(torchletProviderDefinition);
  }
}
