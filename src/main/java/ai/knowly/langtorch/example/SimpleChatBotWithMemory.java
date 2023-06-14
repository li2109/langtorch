package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.integration.openai.SimpleChatCapability;
import ai.knowly.langtorch.llm.openai.OpenAIServiceConfigWithImplicitAPIKeyModule;
import ai.knowly.langtorch.processor.openai.chat.OpenAIChatProcessorConfig;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import com.google.common.flogger.FluentLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import java.io.IOException;

public class SimpleChatBotWithMemory {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) throws IOException {
    // Reading the key from the environment variable under Resource folder(.env file, OPENAI_API_KEY
    // field)
    Injector injector =
        Guice.createInjector(
            new OpenAIServiceConfigWithImplicitAPIKeyModule(),
            new AbstractModule() {
              @Provides
              OpenAIChatProcessorConfig provideOpenAIChatProcessorConfig() {
                return OpenAIChatProcessorConfig.getDefaultInstance();
              }

              @Provides
              ConversationMemory provideConversationMemory() {
                return ConversationMemory.geDefaultInstance();
              }
            });

    SimpleChatCapability chatBot =
        injector.getInstance(SimpleChatCapability.class).withVerboseMode(true);
    readInputUntilEXIT(logger, chatBot);
  }
}
