package ai.knowly.langtorch.llm.huggingface;

import ai.knowly.langtorch.llm.huggingface.schema.config.HuggingFaceServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.HuggingFaceKeyUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class HuggingFaceTestingServiceConfigModule extends AbstractModule {
  private final String modelId;

  public HuggingFaceTestingServiceConfigModule(String modelId) {
    this.modelId = modelId;
  }

  @Provides
  public HuggingFaceServiceConfig provideOpenAIServiceConfig() {
    return HuggingFaceServiceConfig.builder()
        .setApiToken(HuggingFaceKeyUtil.getKey(Environment.TEST))
        .setModelId(modelId)
        .build();
  }
}
