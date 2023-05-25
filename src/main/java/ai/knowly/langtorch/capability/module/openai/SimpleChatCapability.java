package ai.knowly.langtorch.capability.module.openai;

import ai.knowly.langtorch.capability.modality.text.ChatCompletionLLMCapability;
import ai.knowly.langtorch.processor.module.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtorch.preprocessing.parser.ChatMessageToStringParser;
import ai.knowly.langtorch.preprocessing.parser.StringToMultiChatMessageParser;
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
