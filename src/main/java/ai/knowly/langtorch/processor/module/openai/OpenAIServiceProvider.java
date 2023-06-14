package ai.knowly.langtorch.processor.module.openai;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.config.OpenAIServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.OpenAIKeyUtil;
import com.google.common.flogger.FluentLogger;

public final class OpenAIServiceProvider {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private OpenAIServiceProvider() {}

  public static OpenAIService createOpenAIService(String apiKey) {
    return new OpenAIService(OpenAIServiceConfig.builder().setApiKey(apiKey).build());
  }

  public static OpenAIService createOpenAIService() {
    return createOpenAIService(OpenAIKeyUtil.getKey(logger, Environment.PRODUCTION));
  }
}
