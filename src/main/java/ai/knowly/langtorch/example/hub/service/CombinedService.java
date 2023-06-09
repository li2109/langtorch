package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Named;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import com.google.common.flogger.FluentLogger;

@Torchlet
public class CombinedService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final OrderService orderService1;
  private final OrderService orderService2;

  @Inject private OrderService orderService3;

  @Inject
  public CombinedService(
      OrderService orderService1, @Named("orderService") OrderService orderService2) {
    this.orderService1 = orderService1;
    this.orderService2 = orderService2;
  }

  public void test() {
    logger.atInfo().log("CombinedService.test");
  }
}
