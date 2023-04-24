package ai.knowly.langtoch.capability.module.openai.unit;

import ai.knowly.langtoch.capability.unit.CapabilityUnitWithParser;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessorConfig;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.io.MultiChatMessage;

public class SimpleChatCapabilityUnit
    extends CapabilityUnitWithParser<String, MultiChatMessage, ChatMessage, String> {
  private SimpleChatCapabilityUnit(OpenAIChatProcessorConfig openAIChatProcessorConfig) {
    super(OpenAIChatProcessor.create().withConfig(openAIChatProcessorConfig));
  }

  private SimpleChatCapabilityUnit() {
    super(OpenAIChatProcessor.create());
  }

  public static SimpleChatCapabilityUnit create() {
    return new SimpleChatCapabilityUnit();
  }

  public static SimpleChatCapabilityUnit create(
      OpenAIChatProcessorConfig openAIChatProcessorConfig) {
    return new SimpleChatCapabilityUnit(openAIChatProcessorConfig);
  }
}
