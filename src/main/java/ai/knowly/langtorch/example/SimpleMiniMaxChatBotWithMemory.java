package ai.knowly.langtorch.example;

import ai.knowly.langtorch.capability.module.minimax.SimpleMiniMaxChatCapability;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import com.google.common.flogger.FluentLogger;

import java.io.IOException;

import static ai.knowly.langtorch.example.ExampleUtils.readInputUntilEXIT;

/**
 * @author maxiao
 * @date 2023/06/14
 */
public class SimpleMiniMaxChatBotWithMemory {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static void main(String[] args) throws IOException {
        // Reading the key from the environment variable under Resource folder(.env file, MINMAX_GROUP_ID、MINIMAX_API_KEY
        // field)
        SimpleMiniMaxChatCapability chatBot2 = SimpleMiniMaxChatCapability.create()
                .withMemory(ConversationMemory.builder().build())
                .withVerboseMode();

        readInputUntilEXIT(logger, chatBot2);
    }
}
