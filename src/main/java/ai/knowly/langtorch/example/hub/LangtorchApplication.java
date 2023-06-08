package ai.knowly.langtorch.example.hub;

import ai.knowly.langtorch.example.hub.service.CombinedService;
import ai.knowly.langtorch.hub.LangtorchHub;
import ai.knowly.langtorch.hub.annotation.LangtorchHubApplication;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import java.util.Optional;

@LangtorchHubApplication
public class LangtorchApplication {
  public static void main(String[] args) {
    LangtorchHub langtorchHub =
        LangtorchHub.create(LangtorchHubConfig.builder().setVerbose(true).build())
            .run(LangtorchApplication.class);

    Optional<CombinedService> combinedService = langtorchHub.getTorchlet(CombinedService.class);
    combinedService.ifPresent(service -> service.test());
  }
}
