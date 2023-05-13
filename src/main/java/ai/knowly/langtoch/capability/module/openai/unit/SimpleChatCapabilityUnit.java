package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.modality.text.ChatCompletionLLMCapability;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.parser.ChatMessageToStringParser;
import ai.knowly.langtoch.parser.StringToMultiChatMessageParser;
import java.util.Optional;

/** A simple chat capability unit that leverages openai api to generate response */
public class SimpleChatCapabilityUnit extends ChatCompletionLLMCapability<String, String> {

  private SimpleChatCapabilityUnit(OpenAIChatProcessor openAIChatProcessor) {
    super(
        openAIChatProcessor,
        Optional.of(StringToMultiChatMessageParser.create()),
        Optional.of(ChatMessageToStringParser.create()));
  }

  private SimpleChatCapabilityUnit() {
    super(
        OpenAIChatProcessor.create(),
        Optional.of(StringToMultiChatMessageParser.create()),
        Optional.of(ChatMessageToStringParser.create()));
  }

  public static SimpleChatCapabilityUnit create() {
    return new SimpleChatCapabilityUnit();
  }

  public static SimpleChatCapabilityUnit create(OpenAIChatProcessor openAIChatProcessor) {
    return new SimpleChatCapabilityUnit(openAIChatProcessor);
  }

  public static SimpleChatCapabilityUnit create(String openAIKey) {
    return new SimpleChatCapabilityUnit(OpenAIChatProcessor.create(openAIKey));
  }
}
