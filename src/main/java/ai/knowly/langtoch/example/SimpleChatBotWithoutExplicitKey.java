package ai.knowly.langtoch.example;

import ai.knowly.langtoch.capability.module.openai.SimpleChatCapability;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class SimpleChatBotWithoutExplicitKey {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    // Reading the key from the environment variable under Resource folder(.env file, OPENAI_API_KEY
    // field)
    SimpleChatCapability chatBot = SimpleChatCapability.create();
    readInputUntilEXIT(chatBot);
  }

  private static void readInputUntilEXIT(SimpleChatCapability chatBot)
      throws ExecutionException, InterruptedException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      final String sentinel = "EXIT"; // Define a sentinel value to exit the loop
      System.out.printf("Type '%s' and press Enter to exit the application.\n", sentinel);

      while (true) {
        input = reader.readLine();

        if (input == null || sentinel.equalsIgnoreCase(input)) {
          break; // Exit the loop if the user types the sentinel value
        }

        System.out.println("User: " + input);
        String assistantMsg = chatBot.run(input);
        System.out.println("Assistant: " + assistantMsg);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Exiting the application.");
  }
}
