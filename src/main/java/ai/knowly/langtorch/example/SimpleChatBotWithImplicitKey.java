package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.integration.openai.SimpleChatCapability;
import ai.knowly.langtorch.hub.LangtorchHub;
import ai.knowly.langtorch.hub.schema.LangtorchHubConfig;
import ai.knowly.langtorch.hub.schema.OpenAIKeyConfig;
import ai.knowly.langtorch.processor.openai.chat.OpenAIChatProcessorConfig;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import com.google.common.collect.ImmutableList;
import com.google.common.flogger.FluentLogger;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.io.IOException;

public class SimpleChatBotWithImplicitKey {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) throws IOException {
    LangtorchHub langtorchHub =
        new LangtorchHub(
            LangtorchHubConfig.builder()
                .setOpenAIKeyConfig(OpenAIKeyConfig.builder().setReadFromEnvFile(true).build())
                .build(),
            ImmutableList.of(
                new AbstractModule() {
                  @Provides
                  OpenAIChatProcessorConfig provideOpenAIChatProcessorConfig() {
                    return OpenAIChatProcessorConfig.getDefaultInstance();
                  }

                  @Provides
                  ConversationMemory provideConversationMemory() {
                    return ConversationMemory.geDefaultInstance();
                  }
                }));

    SimpleChatCapability chatBot = langtorchHub.getInstance(SimpleChatCapability.class);
    readInputUntilEXIT(logger, chatBot);
  }
}
