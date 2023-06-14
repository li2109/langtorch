package ai.knowly.langtorch.hub.testclass.package2;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;

@TorchletProvider
public class TorchletProviderClass2 {

  @TorchScope(value = TorchScopeValue.PROTOTYPE)
  @Provides
  public TakeoutService2 provideTakeoutService() {
    return new TakeoutService2();
  }
}
