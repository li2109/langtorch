package ai.knowly.langtorch.processor.module.openai;

import ai.knowly.langtorch.llm.openai.OpenAIApi;
import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.utils.ApiKeyUtils;
import com.google.common.flogger.FluentLogger;
import java.time.Duration;
import java.util.Optional;

public final class OpenAIServiceProvider {
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private OpenAIServiceProvider() {}

  public static OpenAIApi createOpenAiAPI(String apiKey) {
    return OpenAIService.buildApi(apiKey, DEFAULT_TIMEOUT);
  }

  public static OpenAIApi createOpenAiAPI() {
    return OpenAIService.buildApi(
        ApiKeyUtils.getOpenAIApiKeyFromEnv(Optional.of(logger)), DEFAULT_TIMEOUT);
  }

  public static OpenAIService createOpenAIService(String apiKey) {
    return new OpenAIService(createOpenAiAPI(apiKey));
  }

  public static OpenAIService createOpenAIService() {
    return new OpenAIService(createOpenAiAPI());
  }
}
