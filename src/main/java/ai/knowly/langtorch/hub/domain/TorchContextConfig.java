package ai.knowly.langtorch.hub.domain;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class TorchContextConfig {
  @Default private boolean verbose = false;
}
