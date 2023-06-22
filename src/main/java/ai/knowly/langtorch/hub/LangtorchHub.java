package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.module.token.TokenUsage;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.google.inject.Key;
import java.lang.annotation.Annotation;
import javax.inject.Inject;

/** LangtorchHub is the entry point for the Langtorch library. */
public class LangtorchHub {
  private final Injector injector;

  @Inject
  public LangtorchHub(LangtorchHubModuleRegistry registry) {
    this.injector = Guice.createInjector(registry.getModules());
  }

  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  public <T> T getInstance(Class<T> clazz, Class<? extends Annotation> annotation) {
    return injector.getInstance(Key.get(clazz, annotation));
  }

  public TokenUsage getTokenUsage() {
    return injector.getInstance(TokenUsage.class);
  }
}
