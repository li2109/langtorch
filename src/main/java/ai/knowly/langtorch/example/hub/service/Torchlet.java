package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;
import ai.knowly.langtorch.hub.schema.TorchScope;
import ai.knowly.langtorch.hub.schema.TorchScopeValue;

@TorchletProvider
public class Torchlet {

  @Provides
  @TorchScope(value = TorchScopeValue.PROTOTYPE)
  public OrderService provideOrderService() {
    return new OrderService();
  }
}
