package ai.knowly.llm.openai;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;

/** A Guice module that provides the OpenAI service. */
public class OpenAIServiceModule extends AbstractModule {
  @Provides
  @Singleton
  public OpenAiService provideOpenAIModel() {
    Dotenv dotenv = Dotenv.load();
    return new OpenAiService(Objects.requireNonNull(dotenv.get("OPENAI_API_KEY")));
  }
}
