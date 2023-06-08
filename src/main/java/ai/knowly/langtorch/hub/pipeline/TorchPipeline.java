package ai.knowly.langtorch.hub.pipeline;

import ai.knowly.langtorch.hub.annotation.TorchInject;

public class TorchPipeline {
  @TorchInject
  public TorchPipeline() {
    System.out.println("TorchPipeline");
  }
}
