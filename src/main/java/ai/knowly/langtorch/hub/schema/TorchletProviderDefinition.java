package ai.knowly.langtorch.hub.schema;

import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TorchletProviderDefinition {
  private Class<?> providerClass;
  private Method method;
  private TorchScopeValue scope;
}
