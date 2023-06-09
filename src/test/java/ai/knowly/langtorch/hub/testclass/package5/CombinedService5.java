package ai.knowly.langtorch.hub.testclass.package5;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Torchlet;

@Torchlet
public class CombinedService5 {
  private TakeoutService5 takeoutService5FromConstructor;

  @Inject private TakeoutService5 takeoutService5FromField;

  @Inject
  public CombinedService5(TakeoutService5 takeoutService4) {
    this.takeoutService5FromConstructor = takeoutService4;
  }

  public TakeoutService5 getTakeoutService5FromConstructor() {
    return takeoutService5FromConstructor;
  }

  public TakeoutService5 getTakeoutService5FromField() {
    return takeoutService5FromField;
  }
}
