package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.integration.openai.SimpleChatCapability;
import ai.knowly.langtorch.hub.LangtorchHub;
import ai.knowly.langtorch.hub.LangtorchHubModuleRegistry;
import ai.knowly.langtorch.hub.module.token.TokenUsage;
import ai.knowly.langtorch.hub.schema.OpenAIKeyConfig;
import com.google.common.flogger.FluentLogger;
import java.io.IOException;

public class SimpleChatBotWithExplicitKey {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) throws IOException {
    String openAIKey = "YOUR_OPENAI_API_KEY";
    LangtorchHubModuleRegistry registry = LangtorchHubModuleRegistry.create();
    registry.registerOpenAiModule(OpenAIKeyConfig.createOpenConfigWithApiKey(openAIKey));
    LangtorchHub langtorchHub = new LangtorchHub(registry);

    SimpleChatCapability chatBot = langtorchHub.getInstance(SimpleChatCapability.class);
    readInputUntilEXIT(logger, chatBot);
    TokenUsage tokenUsage = langtorchHub.getTokenUsage();
    logger.atInfo().log(
        "Prompt token usage: %s, Completion token usage: %s",
        tokenUsage.getPromptTokenUsage(), tokenUsage.getCompletionTokenUsage());
  }
}
