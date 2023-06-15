package ai.knowly.langtorch.llm.openai.modules;

import ai.knowly.langtorch.llm.openai.schema.config.OpenAIServiceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/** Provides the OpenAI service configuration. */
public class OpenAIServiceConfigWithExplicitAPIKeyModule extends AbstractModule {
  private final String apikey;

  public OpenAIServiceConfigWithExplicitAPIKeyModule(String apikey) {
    this.apikey = apikey;
  }

  @Provides
  public OpenAIServiceConfig provideOpenAIServiceConfig() {
    return OpenAIServiceConfig.builder().setApiKey(apikey).build();
  }
}
