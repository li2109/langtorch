package ai.knowly.langtorch.hub.schema;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class LangtorchHubConfig {
  @Default private boolean verbose = false;
}
