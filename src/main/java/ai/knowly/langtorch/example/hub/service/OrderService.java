package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Torchlet;
import com.google.common.flogger.FluentLogger;

@Torchlet
public class OrderService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public void order() {
    logger.atInfo().log("OrderService with @Torchlet");
  }
}
