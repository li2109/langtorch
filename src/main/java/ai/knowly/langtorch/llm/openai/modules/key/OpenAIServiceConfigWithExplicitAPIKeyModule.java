package ai.knowly.langtorch.llm.openai.modules.key;

import ai.knowly.langtorch.llm.openai.schema.config.OpenAIServiceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * This Java class provides an OpenAIServiceConfig object with an explicit API key.
 */
public class OpenAIServiceConfigWithExplicitAPIKeyModule extends AbstractModule {
  private final String apikey;

  public OpenAIServiceConfigWithExplicitAPIKeyModule(String apikey) {
    this.apikey = apikey;
  }

/**
 * This Java function provides an OpenAIServiceConfig object with an API key.
 * 
 * @return An instance of the `OpenAIServiceConfig` class with the API key set.
 */
  @Provides
  public OpenAIServiceConfig provideOpenAIServiceConfig() {
    return OpenAIServiceConfig.builder().setApiKey(apikey).build();
  }
}
