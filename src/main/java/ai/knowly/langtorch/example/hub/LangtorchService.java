package ai.knowly.langtorch.example.hub;

import ai.knowly.langtorch.example.hub.service.D;
import ai.knowly.langtorch.example.hub.service.F;
import ai.knowly.langtorch.hub.LangtorchHub;
import ai.knowly.langtorch.hub.annotation.TorchHub;
import ai.knowly.langtorch.hub.domain.TorchContextConfig;
import com.google.common.flogger.FluentLogger;

@TorchHub
public class LangtorchService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) {
    LangtorchHub langtorchHub =
        new LangtorchHub(TorchContextConfig.builder().setVerbose(true).build())
            .run(LangtorchService.class);

    D d = (D) langtorchHub.getTorchlet(D.class);
    logger.atInfo().log("torchlet d: %s\n", d);
    logger.atInfo().log("torchlet c from d: %s\n", d.getC());

    D d1 = (D) langtorchHub.getTorchlet(D.class);
    logger.atInfo().log("torchlet d1: %s\n", d1);
    logger.atInfo().log("torchlet c from d: %s\n", d1.getC());

    F f = (F) langtorchHub.getTorchlet(F.class);
    logger.atInfo().log("torchlet f: %s\n", f);
    logger.atInfo().log("torchlet c from f: %s\n", f.getC());

    F f1 = (F) langtorchHub.getTorchlet(F.class);
    logger.atInfo().log("torchlet f1: %s\n", f);
    logger.atInfo().log("torchlet c from f1: %s\n", f1.getC());
  }
}
