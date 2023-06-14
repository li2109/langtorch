package ai.knowly.langtorch.capability.integration.openai;

import ai.knowly.langtorch.capability.modality.text.ChatCompletionLLMCapability;
import ai.knowly.langtorch.capability.modality.text.Parsers;
import ai.knowly.langtorch.preprocessing.parser.ChatMessageToStringParser;
import ai.knowly.langtorch.preprocessing.parser.StringToMultiChatMessageParser;
import ai.knowly.langtorch.processor.module.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import javax.inject.Inject;

/** A simple chat capability unit that leverages openai api to generate response */
public class SimpleChatCapability extends ChatCompletionLLMCapability<String, String> {
  @Inject
  public SimpleChatCapability(OpenAIChatProcessor openAIChatProcessor, ConversationMemory memory) {
    super(
        openAIChatProcessor,
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
