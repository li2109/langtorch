package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.integration.openai.SimpleChatCapability;
import ai.knowly.langtorch.llm.openai.OpenAIServiceConfigWithExplicitAPIKeyModule;
import ai.knowly.langtorch.processor.module.openai.chat.OpenAIChatProcessorConfig;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import com.google.common.flogger.FluentLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import java.io.IOException;

public class SimpleChatBotWithExplicitKey {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) throws IOException {
    String openAIKey = "random_key";
    Injector injector =
        Guice.createInjector(
            new OpenAIServiceConfigWithExplicitAPIKeyModule(openAIKey),
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
