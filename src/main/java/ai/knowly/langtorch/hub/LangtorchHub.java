package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.llm.openai.modules.OpenAIServiceConfigWithExplicitAPIKeyModule;
import ai.knowly.langtorch.llm.openai.modules.OpenAIServiceConfigWithImplicitAPIKeyModule;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/** LangtorchHub is the entry point for the Langtorch library. */
public class LangtorchHub {
  private final Injector injector;

  public LangtorchHub(LangtorchHubConfig config, ImmutableList<AbstractModule> modules) {
    ImmutableList.Builder<AbstractModule> moduleBuilder = ImmutableList.builder();
    moduleBuilder.addAll(modules);

    // If the user has provided an API key, use it. Otherwise, try to get it from the environment.
    if (config.getOpenAiApiKey().isPresent()) {
      moduleBuilder.add(
          new OpenAIServiceConfigWithExplicitAPIKeyModule(config.getOpenAiApiKey().get()));
    } else {
      moduleBuilder.add(new OpenAIServiceConfigWithImplicitAPIKeyModule());
    }

    this.injector = Guice.createInjector(moduleBuilder.build());
  }

  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }
}
