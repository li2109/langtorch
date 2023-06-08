package ai.knowly.langtorch.hub.testclass;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;

@TorchletProvider
public class TorchletProviderClass {

  @Provides
  public OrderService provideOrderService() {
    return new OrderService();
  }

  @TorchScope(value = TorchScopeValue.PROTOTYPE)
  @Provides
  public TakeoutService provideTakeoutService() {
    return new TakeoutService();
  }
}
