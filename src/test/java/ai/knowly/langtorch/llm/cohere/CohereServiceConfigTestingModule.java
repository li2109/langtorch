package ai.knowly.langtorch.llm.cohere;

import ai.knowly.langtorch.llm.cohere.schema.config.CohereAIServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.CohereKeyUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class CohereServiceConfigTestingModule extends AbstractModule {
  // Get the Cohere key from the environment variables and provide it to the Cohere service.
  @Provides
  public CohereAIServiceConfig provideCohereServiceConfig() {
    return CohereAIServiceConfig.builder()
        .setApiKey(CohereKeyUtil.getKey((Environment.TEST)))
        .build();
  }
}
