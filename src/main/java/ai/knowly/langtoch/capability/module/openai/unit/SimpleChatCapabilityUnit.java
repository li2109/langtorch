package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnit;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.io.MultiChatMessage;

/** A simple chat capability unit that leverages openai api to generate response */
public class SimpleChatCapabilityUnit
    extends CapabilityUnit<MultiChatMessage, MultiChatMessage, ChatMessage, ChatMessage> {

  private SimpleChatCapabilityUnit(OpenAIChatProcessor openAIChatProcessor) {
    super(openAIChatProcessor);
  }

  private SimpleChatCapabilityUnit() {
    super(OpenAIChatProcessor.create());
  }

  public static SimpleChatCapabilityUnit create() {
    return new SimpleChatCapabilityUnit();
  }

  public static SimpleChatCapabilityUnit create(OpenAIChatProcessor openAIChatProcessor) {
    return new SimpleChatCapabilityUnit(openAIChatProcessor);
  }
}
