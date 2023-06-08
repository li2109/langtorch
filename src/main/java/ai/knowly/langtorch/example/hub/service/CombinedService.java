package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Torchlet;
import com.google.common.flogger.FluentLogger;

@Torchlet
public class CombinedService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final OrderService orderService;
  private final TakeoutService takeoutService;

  @Inject
  public CombinedService(OrderService orderService, TakeoutService takeoutService) {
    this.orderService = orderService;
    this.takeoutService = takeoutService;
  }

  public void test() {
    logger.atInfo().log("CombinedService.test");
    orderService.order();
    takeoutService.takeout();
  }
}
