//package ai.knowly.langtoch.llm.provider.openai;
//
//import ai.knowly.langtoch.llm.Utils;
//import com.google.common.flogger.FluentLogger;
//import com.google.inject.AbstractModule;
//import com.google.inject.Provides;
//import com.google.inject.Singleton;
//import com.theokanning.openai.service.OpenAiService;
//import io.github.cdimascio.dotenv.Dotenv;
//import java.util.Objects;
//
///** A Guice module that provides the OpenAI service. */
//public class OpenAIServiceModule extends AbstractModule {
//  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
//
//  @Provides
//  @Singleton
//  public OpenAiService provideOpenAIModel() {
//    Dotenv dotenv = Dotenv.load();
//    String openaiApiKey = Objects.requireNonNull(dotenv.get("OPENAI_API_KEY"));
//    Utils.logPartialOpenAIApiKey(logger, openaiApiKey);
//    return new OpenAiService(openaiApiKey);
//  }
//}
