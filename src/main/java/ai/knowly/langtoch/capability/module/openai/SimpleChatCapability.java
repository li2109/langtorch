package ai.knowly.langtoch.capability.module.openai;

import ai.knowly.langtoch.capability.modality.text.ChatCompletionLLMCapability;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.parser.ChatMessageToStringParser;
import ai.knowly.langtoch.parser.StringToMultiChatMessageParser;
import java.util.Optional;

/** A simple chat capability unit that leverages openai api to generate response */
public class SimpleChatCapability extends ChatCompletionLLMCapability<String, String> {

  private SimpleChatCapability(OpenAIChatProcessor openAIChatProcessor) {
    super(
        openAIChatProcessor,
        Optional.of(StringToMultiChatMessageParser.create()),
        Optional.of(ChatMessageToStringParser.create()));
  }

  private SimpleChatCapability() {
    super(
        OpenAIChatProcessor.create(),
        Optional.of(StringToMultiChatMessageParser.create()),
        Optional.of(ChatMessageToStringParser.create()));
  }

  public static SimpleChatCapability create() {
    return new SimpleChatCapability();
  }

  public static SimpleChatCapability create(OpenAIChatProcessor openAIChatProcessor) {
    return new SimpleChatCapability(openAIChatProcessor);
  }

  public static SimpleChatCapability create(String openAIKey) {
    return new SimpleChatCapability(OpenAIChatProcessor.create(openAIKey));
  }
}
