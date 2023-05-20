package ai.knowly.langtorch.example;

import ai.knowly.langtorch.capability.module.openai.SimpleChatCapability;
import com.google.common.flogger.FluentLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleChatBotWithoutMemory {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  public static void main(String[] args) {
    String openAIKey = "RandomKey";
    SimpleChatCapability chatBot = SimpleChatCapability.create(openAIKey);
    readInputUntilEXIT(chatBot);
  }

  private static void readInputUntilEXIT(SimpleChatCapability chatBot) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      final String sentinel = "EXIT"; // Define a sentinel value to exit the loop

      logger.atInfo().log("Type '%s' and press Enter to exit the application.%n", sentinel);

      while (true) {
        input = reader.readLine();

        if (input == null || sentinel.equalsIgnoreCase(input)) {
          break; // Exit the loop if the user types the sentinel value
        }

        logger.atInfo().log("User: " + input);
        String assistantMsg = chatBot.run(input);
        logger.atInfo().log("Assistant: " + assistantMsg);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    logger.atInfo().log("Exiting the application.");
  }
}
