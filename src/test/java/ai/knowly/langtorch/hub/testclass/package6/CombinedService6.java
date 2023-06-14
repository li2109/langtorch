package ai.knowly.langtorch.hub.testclass.package6;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Named;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import ai.knowly.langtorch.hub.testclass.package6.torchletProvider6.TakeoutService6;

@Torchlet
public class CombinedService6 {
  private TakeoutService6 takeoutService6FromConstructor;

  @Inject
  @Named(value = "takeout-service-6-a")
  private TakeoutService6 takeoutService6FromField;

  @Inject
  public CombinedService6(@Named(value = "takeout-service-6-b") TakeoutService6 takeoutService6) {
    this.takeoutService6FromConstructor = takeoutService6;
  }

  public TakeoutService6 getTakeoutService6FromConstructor() {
    return takeoutService6FromConstructor;
  }

  public TakeoutService6 getTakeoutService6FromField() {
    return takeoutService6FromField;
  }
}
