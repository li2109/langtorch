package ai.knowly.langtoch.llm.processor.openai;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAiApi;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAiService;
import com.google.common.flogger.FluentLogger;
import java.time.Duration;

public final class OpenAIServiceProvider {
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static OpenAiApi createOpenAiAPI(String apiKey) {
    return OpenAiService.buildApi(apiKey, DEFAULT_TIMEOUT);
  }

  public static OpenAiApi createOpenAiAPI() {
    return OpenAiService.buildApi(Utils.getOpenAIApiKeyFromEnv(logger), DEFAULT_TIMEOUT);
  }

  public static OpenAiService createOpenAIService(String apiKey) {
    return new OpenAiService(createOpenAiAPI(apiKey));
  }

  public static OpenAiService createOpenAIService() {
    return new OpenAiService(createOpenAiAPI());
  }
}
