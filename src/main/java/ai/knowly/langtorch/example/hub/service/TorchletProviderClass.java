package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.schema.TorchScope;

@TorchletProvider
public class TorchletProviderClass {

  @Provides("orderService2")
  @TorchScope
  public OrderService provideOrderService2() {
    return new OrderService();
  }

  @Provides("orderService3")
  @TorchScope
  public OrderService provideOrderService3() {
    return new OrderService();
  }
}
