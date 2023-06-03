package ai.knowly.langtorch.processor.module.openai;

import ai.knowly.langtorch.llm.openai.OpenAIApi;
import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.OpenAIKeyUtil;
import com.google.common.flogger.FluentLogger;
import java.time.Duration;

public final class OpenAIServiceProvider {
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private OpenAIServiceProvider() {}

  public static OpenAIApi createOpenAiAPI(String apiKey) {
    return OpenAIService.buildApi(apiKey, DEFAULT_TIMEOUT);
  }

  public static OpenAIApi createOpenAiAPI() {
    return OpenAIService.buildApi(
        OpenAIKeyUtil.getKey(logger, Environment.PRODUCTION), DEFAULT_TIMEOUT);
  }

  public static OpenAIService createOpenAIService(String apiKey) {
    return OpenAIService.create(createOpenAiAPI(apiKey));
  }

  public static OpenAIService createOpenAIService() {
    return OpenAIService.create(createOpenAiAPI());
  }
}
