package ai.knowly.langtorch.hub.schema;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Torchlet definition contains information about the Torchlet. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TorchletDefinition {
  private Class<?> clazz;
  private TorchScopeValue scope;
}
