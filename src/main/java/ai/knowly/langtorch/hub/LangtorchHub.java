package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.module.token.TokenUsage;
import com.google.inject.Guice;
import com.google.inject.Injector;

/** LangtorchHub is the entry point for the Langtorch library. */
public class LangtorchHub {
  private final Injector injector;

  public LangtorchHub(LangtorchHubModuleRegistry registry) {
    this.injector = Guice.createInjector(registry.getModules());
  }

  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  public TokenUsage getTokenUsage() {
    return injector.getInstance(TokenUsage.class);
  }
}
