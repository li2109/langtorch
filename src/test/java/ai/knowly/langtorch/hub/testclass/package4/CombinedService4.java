package ai.knowly.langtorch.hub.testclass.package4;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Torchlet;

@Torchlet
public class CombinedService4 {
  private TakeoutService4 takeoutService4FromConstructor;

  @Inject private TakeoutService4 takeoutService4FromField;

  @Inject
  public CombinedService4(TakeoutService4 takeoutService4) {
    this.takeoutService4FromConstructor = takeoutService4;
  }

  public TakeoutService4 getTakeoutService4FromConstructor() {
    return takeoutService4FromConstructor;
  }

  public TakeoutService4 getTakeoutService4FromField() {
    return takeoutService4FromField;
  }
}
