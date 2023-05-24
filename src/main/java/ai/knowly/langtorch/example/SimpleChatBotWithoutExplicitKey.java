package ai.knowly.langtorch.example;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

import ai.knowly.langtorch.capability.module.openai.SimpleChatCapability;
import com.google.common.flogger.FluentLogger;
import java.io.IOException;

public class SimpleChatBotWithoutExplicitKey {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) throws IOException {
    // Reading the key from the environment variable under Resource folder(.env file, OPENAI_API_KEY
    // field)
    SimpleChatCapability chatBot = SimpleChatCapability.create();
    readInputUntilEXIT(logger, chatBot);
  }
}
