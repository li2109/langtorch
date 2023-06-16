package ai.knowly.langtorch.processor.cohere;

import ai.knowly.langtorch.llm.cohere.CohereApiService;
import ai.knowly.langtorch.llm.cohere.schema.config.CohereServiceConfig;
import ai.knowly.langtorch.processor.cohere.generate.CohereGenerateProcessorConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.CohereKeyUtil;
import com.google.common.flogger.FluentLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public final class CohereProcessorModule extends AbstractModule {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Provides
  public CohereApiService providesCohereAPI() {
    return new CohereApiService(
        CohereServiceConfig.builder()
            .setApiKey(CohereKeyUtil.getApiKey(logger, Environment.PRODUCTION))
            .build());
  }

  @Provides
  public CohereGenerateProcessorConfig providesCohereGenerateProcessorConfig() {
    return CohereGenerateProcessorConfig.builder().build();
  }
}
