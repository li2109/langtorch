package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.module.openai.SimpleChatCapability;
import com.google.common.flogger.FluentLogger;

public class SimpleChatBotWithoutMemory {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) {
    String openAIKey = "RandomKey";
    SimpleChatCapability chatBot = SimpleChatCapability.create(openAIKey);
    readInputUntilEXIT(logger, chatBot);
  }
}
