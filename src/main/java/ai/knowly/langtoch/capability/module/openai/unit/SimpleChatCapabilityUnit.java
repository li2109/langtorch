package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.chat.Message;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.io.MultiChatMessage;

/** A simple chat capability unit that leverages openai api to generate response */
public class SimpleChatCapabilityUnit
    extends CapabilityUnit<String, MultiChatMessage, ChatMessage, String> {

  private SimpleChatCapabilityUnit(OpenAIChatProcessor openAIChatProcessor) {
    super(openAIChatProcessor);
    super.withInputParser((input) -> MultiChatMessage.of(ChatMessage.of(Role.USER, input)))
        .withOutputParser(Message::getMessage);
  }

  private SimpleChatCapabilityUnit() {
    super(OpenAIChatProcessor.create());
    super.withInputParser((input) -> MultiChatMessage.of(ChatMessage.of(Role.USER, input)))
        .withOutputParser(Message::getMessage);
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
