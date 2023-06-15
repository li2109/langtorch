package ai.knowly.langtorch.capability.module.minimax;

import ai.knowly.langtorch.capability.modality.text.MiniMaxChatCompletionLLMCapability;
import ai.knowly.langtorch.preprocessing.parser.ChatMessageToStringParser;
import ai.knowly.langtorch.preprocessing.parser.Parser;
import ai.knowly.langtorch.preprocessing.parser.StringToMultiChatMessageParser;
import ai.knowly.langtorch.processor.module.minimax.chat.MiniMaxChatProcessor;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import java.util.Optional;

/**
 * A simple chat capability unit that leverages minimax api to generate response
 *
 * @author maxiao
 * @date 2023/06/13
 */
public class SimpleMiniMaxChatCapability
    extends MiniMaxChatCompletionLLMCapability<String, String> {

  private SimpleMiniMaxChatCapability(MiniMaxChatProcessor miniMaxChatProcessor) {
    super(
        miniMaxChatProcessor,
        Optional.of(StringToMultiChatMessageParser.create()),
        Optional.of(ChatMessageToStringParser.create()));
  }

  private SimpleMiniMaxChatCapability() {
    super(
        MiniMaxChatProcessor.create(),
        Optional.of(StringToMultiChatMessageParser.create()),
        Optional.of(ChatMessageToStringParser.create()));
  }

  public static SimpleMiniMaxChatCapability create() {
    return new SimpleMiniMaxChatCapability();
  }

  public static SimpleMiniMaxChatCapability create(MiniMaxChatProcessor MiniMaxChatProcessor) {
    return new SimpleMiniMaxChatCapability(MiniMaxChatProcessor);
  }

  public static SimpleMiniMaxChatCapability create(String groupId, String apiKey) {
    return new SimpleMiniMaxChatCapability(MiniMaxChatProcessor.create(groupId, apiKey));
  }

  public SimpleMiniMaxChatCapability withMemory(ConversationMemory conversationMemory) {
    super.withMemory(conversationMemory);
    return this;
  }

  @Override
  public SimpleMiniMaxChatCapability withVerboseMode() {
    super.withVerboseMode();
    return this;
  }

  @Override
  public MiniMaxChatCompletionLLMCapability<String, String> withInputParser(
      Parser<String, MultiChatMessage> inputParser) {
    super.withInputParser(inputParser);
    return this;
  }

  @Override
  public MiniMaxChatCompletionLLMCapability<String, String> withOutputParser(
      Parser<ChatMessage, String> outputParser) {
    super.withOutputParser(outputParser);
    return this;
  }
}
