package ai.knowly.langtorch.example.hub;

import ai.knowly.langtorch.example.hub.service.CombinedService;
import ai.knowly.langtorch.example.hub.service.OrderService;
import ai.knowly.langtorch.hub.LangtorchHub;
import ai.knowly.langtorch.hub.annotation.LangtorchHubApplication;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;

@LangtorchHubApplication
public class LangtorchApplication {
  public static void main(String[] args) {
    LangtorchHub langtorchHub =
        LangtorchHub.create(LangtorchHubConfig.builder().setVerbose(true).build())
            .run(LangtorchApplication.class);

    Object torchlet1 = langtorchHub.getTorchlet(CombinedService.class);
    Object torchlet2 = langtorchHub.getTorchlet(CombinedService.class);

    System.out.println(langtorchHub.getTorchlet(OrderService.class));
    System.out.println(langtorchHub.getTorchlet(OrderService.class));
  }
}
