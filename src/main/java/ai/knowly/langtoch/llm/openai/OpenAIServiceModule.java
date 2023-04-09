package ai.knowly.langtoch.llm.openai;

import com.google.common.flogger.FluentLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;

/** A Guice module that provides the OpenAI service. */
public class OpenAIServiceModule extends AbstractModule {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Provides
  @Singleton
  public OpenAiService provideOpenAIModel() {
    Dotenv dotenv = Dotenv.load();
    String openaiApiKey = Objects.requireNonNull(dotenv.get("OPENAI_API_KEY"));
    Utils.logPartialApiKey(logger, openaiApiKey);
    return new OpenAiService(openaiApiKey);
  }
}
