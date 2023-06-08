package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.schema.TorchContextConfig;

/** LangtorchHub is the entry point for the Langtorch framework. */
public class LangtorchHub {
  private final TorchContext torchContext;

  public LangtorchHub(TorchContextConfig torchContextConfig) {
    this.torchContext = new TorchContext(torchContextConfig);
  }

  public LangtorchHub run(Class<?> clazz) {
    torchContext.init(clazz);
    return this;
  }

  public Object getTorchlet(String name) {
    return torchContext.getTorchlet(name);
  }

  public Object getTorchlet(Class<?> clazz) {
    return torchContext.getTorchlet(clazz);
  }
}
