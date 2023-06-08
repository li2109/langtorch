package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;

/** LangtorchHub is the entry point for the Langtorch framework. */
public class LangtorchHub {
  private final LangtorchContext langtorchContext;

  public LangtorchHub(LangtorchHubConfig langtorchHubConfig) {
    this.langtorchContext = new LangtorchContext(langtorchHubConfig);
  }

  public LangtorchHub run(Class<?> clazz) {
    langtorchContext.init(clazz);
    return this;
  }

  public Object getTorchlet(String name) {
    return langtorchContext.getTorchlet(name);
  }

  public Object getTorchlet(Class<?> clazz) {
    return langtorchContext.getTorchlet(clazz);
  }
}
