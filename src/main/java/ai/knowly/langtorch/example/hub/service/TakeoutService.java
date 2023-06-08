package ai.knowly.langtorch.example.hub.service;

import ai.knowly.langtorch.hub.annotation.Torchlet;
import com.google.common.flogger.FluentLogger;

@Torchlet
public class TakeoutService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public void takeout() {
    logger.atInfo().log("TakeoutService.takeout");
  }
}
