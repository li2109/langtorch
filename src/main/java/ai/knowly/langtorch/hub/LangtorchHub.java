package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/** LangtorchHub is the entry point for the Langtorch framework. */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LangtorchHub {
  private final LangtorchContext langtorchContext;

  private LangtorchHub(LangtorchHubConfig langtorchHubConfig) {
    this.langtorchContext = new LangtorchContext(langtorchHubConfig);
  }

  public static LangtorchHub create(LangtorchHubConfig langtorchHubConfig) {
    return new LangtorchHub(langtorchHubConfig);
  }

  public LangtorchHub run(Class<?> clazz) {
    langtorchContext.init(clazz);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> getTorchlet(String name) {
    return Optional.ofNullable((T) langtorchContext.getTorchlet(name));
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> getTorchlet(Class<?> clazz) {
    return Optional.ofNullable((T) langtorchContext.getTorchlet(clazz));
  }
}
