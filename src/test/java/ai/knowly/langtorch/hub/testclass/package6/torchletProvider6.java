package ai.knowly.langtorch.hub.testclass.package6;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;

@TorchletProvider
public class torchletProvider6 {

  @Provides(value = "takeout-service-6-a")
  public TakeoutService6 provideTakeoutService6a() {
    return new TakeoutService6();
  }

  @Provides(value = "takeout-service-6-b")
  public TakeoutService6 provideTakeoutService6b() {
    return new TakeoutService6();
  }

  public static class TakeoutService6 {}
}
