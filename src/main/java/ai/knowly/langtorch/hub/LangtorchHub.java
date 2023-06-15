package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.schema.OpenAIKeyConfig;
import ai.knowly.langtorch.llm.openai.modules.key.OpenAIServiceConfigWithExplicitAPIKeyModule;
import ai.knowly.langtorch.llm.openai.modules.key.OpenAIServiceConfigWithImplicitAPIKeyModule;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/** LangtorchHub is the entry point for the Langtorch library. */
public class LangtorchHub {
  private final Injector injector;

  public LangtorchHub(LangtorchHubConfig config, ImmutableList<AbstractModule> extraModules) {
    ImmutableList.Builder<AbstractModule> moduleBuilder = ImmutableList.builder();
    moduleBuilder.addAll(extraModules);

    // If the OpenAI config is present, add the OpenAI module to the module builder.
    config
        .getOpenAIKeyConfig()
        .ifPresent(openAIKeyConfig -> moduleBuilder.add(getOpenAIModule(openAIKeyConfig)));

    this.injector = Guice.createInjector(moduleBuilder.build());
  }

  private AbstractModule getOpenAIModule(OpenAIKeyConfig openAIKeyConfig) {
    if (openAIKeyConfig.isReadFromEnvFile()) {
      return new OpenAIServiceConfigWithImplicitAPIKeyModule();
    }
    if (!openAIKeyConfig.getOpenAiApiKey().isPresent()) {
      throw new IllegalArgumentException(
          "OpenAI API key is not present. Please provide the API key in the config.");
    }

    return new OpenAIServiceConfigWithExplicitAPIKeyModule(openAIKeyConfig.getOpenAiApiKey().get());
  }

  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }
}
