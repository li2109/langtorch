package ai.knowly.langtorch.capability.integration.minimax;

import ai.knowly.langtorch.capability.modality.text.MiniMaxChatCompletionLLMCapability;
import ai.knowly.langtorch.capability.modality.text.Parsers;
import ai.knowly.langtorch.preprocessing.parser.ChatMessageToStringParser;
import ai.knowly.langtorch.preprocessing.parser.StringToMultiChatMessageParser;
import ai.knowly.langtorch.processor.minimax.chat.MiniMaxChatProcessor;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;

/**
 * A simple chat capability unit that leverages minimax api to generate response
 *
 * @author maxiao
 * @date 2023/06/13
 */
public class SimpleChatCapability extends MiniMaxChatCompletionLLMCapability<String, String> {

  private SimpleChatCapability(
      MiniMaxChatProcessor miniMaxChatProcessor, ConversationMemory memory) {
    super(
        miniMaxChatProcessor,
        Parsers.<String, MultiChatMessage, ChatMessage, String>builder()
            .setInputParser(StringToMultiChatMessageParser.create())
            .setOutputParser(ChatMessageToStringParser.create())
            .build(),
        memory);
  }

  @Override
  public SimpleChatCapability withVerboseMode(boolean verboseMode) {
    super.withVerboseMode(verboseMode);
    return this;
  }
}
