package ai.knowly.langtorch.llm.openai;

import ai.knowly.langtorch.llm.openai.schema.config.OpenAIServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.OpenAIKeyUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/** Provides the OpenAI service configuration. */
public class OpenAIServiceConfigModule extends AbstractModule {

  // Get the OpenAI key from the environment variables and provide it to the OpenAI service.
  @Provides
  public OpenAIServiceConfig provideOpenAIServiceConfig() {
    return OpenAIServiceConfig.builder()
        .setApiKey(OpenAIKeyUtil.getKey(Environment.PRODUCTION))
        .build();
  }
}
