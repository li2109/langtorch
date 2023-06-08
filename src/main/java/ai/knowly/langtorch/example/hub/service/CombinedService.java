package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Inject;
import ai.knowly.langtorch.hub.annotation.Torchlet;

@Torchlet
public class CombinedService {
  private final OrderService orderService;
  private final TakeoutService takeoutService;

  @Inject
  public CombinedService(OrderService orderService, TakeoutService takeoutService) {
    this.orderService = orderService;
    this.takeoutService = takeoutService;
  }

  public void test() {
    System.out.println("CombinedService.test");
    orderService.order();
    takeoutService.takeout();
  }
}
