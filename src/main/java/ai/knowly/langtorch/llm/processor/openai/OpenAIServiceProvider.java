package ai.knowly.langtorch.llm.processor.openai;

import ai.knowly.langtorch.llm.Utils;
import ai.knowly.langtorch.llm.integration.openai.service.OpenAIApi;
import ai.knowly.langtorch.llm.integration.openai.service.OpenAIService;
import com.google.common.flogger.FluentLogger;
import java.time.Duration;
import java.util.Optional;

public final class OpenAIServiceProvider {
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static OpenAIApi createOpenAiAPI(String apiKey) {
    return OpenAIService.buildApi(apiKey, DEFAULT_TIMEOUT);
  }

  public static OpenAIApi createOpenAiAPI() {
    return OpenAIService.buildApi(
        Utils.getOpenAIApiKeyFromEnv(Optional.of(logger)), DEFAULT_TIMEOUT);
  }

  public static OpenAIService createOpenAIService(String apiKey) {
    return new OpenAIService(createOpenAiAPI(apiKey));
  }

  public static OpenAIService createOpenAIService() {
    return new OpenAIService(createOpenAiAPI());
  }
}
