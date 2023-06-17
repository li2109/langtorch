package ai.knowly.langtorch.llm.openai.modules.key;

import ai.knowly.langtorch.llm.openai.schema.config.OpenAIServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.OpenAIKeyUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * This Java class provides the OpenAI key from environment variables to the OpenAI service
 * configuration.
 */
public class OpenAIServiceConfigWithImplicitAPIKeyModule extends AbstractModule {
  
  /**
   * This function provides an OpenAIServiceConfig object with an API key set based on the current
   * environment.
   * 
   * @return An instance of the `OpenAIServiceConfig` class is being returned with the API key read from the environment variable.
   */
  @Provides
  public OpenAIServiceConfig provideOpenAIServiceConfig() {
    return OpenAIServiceConfig.builder()
        .setApiKey(OpenAIKeyUtil.getKey(Environment.PRODUCTION))
        .build();
  }
}
