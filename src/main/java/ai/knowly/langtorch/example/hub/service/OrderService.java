package ai.knowly.langtorch.example.hub.service;

import com.google.common.flogger.FluentLogger;

public class OrderService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public void order() {
    logger.atInfo().log("OrderService.order");
  }
}
