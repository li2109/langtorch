package ai.knowly.langtorch.hub.testclass.package1;

import ai.knowly.langtorch.hub.annotation.Provides;
import ai.knowly.langtorch.hub.annotation.TorchletProvider;

@TorchletProvider
public class TorchletProviderClass1 {

  @Provides
  public OrderService1 provideOrderService() {
    return new OrderService1();
  }
}
