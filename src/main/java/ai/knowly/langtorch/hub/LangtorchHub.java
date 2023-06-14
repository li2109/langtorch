package ai.knowly.langtorch.hub;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/** LangtorchHub is the main entry point for using Langtorch framework. */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LangtorchHub {
  private Injector injector;

  /** Create a new LangtorchHub instance. */
  public static LangtorchHub create(ImmutableList<AbstractModule> modules) {
    return new LangtorchHub(Guice.createInjector(new LangtorchHubModule()));
  }

  public <T> T get(Key<T> key) {
    return injector.getInstance(key);
  }
}
