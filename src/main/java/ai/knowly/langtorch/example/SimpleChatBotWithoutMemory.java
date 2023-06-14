package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.module.openai.SimpleChatCapability;
import com.google.common.flogger.FluentLogger;
import java.io.IOException;

public class SimpleChatBotWithoutMemory {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) throws IOException {
    String openAIKey = "RandomKey";
    SimpleChatCapability chatBot = SimpleChatCapability.create(openAIKey);
    readInputUntilEXIT(logger, chatBot);
  }
}
