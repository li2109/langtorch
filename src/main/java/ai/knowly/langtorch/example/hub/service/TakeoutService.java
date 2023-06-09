package ai.knowly.langtorch.example.hub.service;

import com.google.common.flogger.FluentLogger;

// @Torchlet
public class TakeoutService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public void takeout() {
    logger.atInfo().log("TakeoutService.takeout");
  }
}
