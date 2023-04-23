package ai.knowly.langtoch.llm.provider.openai;

import static ai.knowly.langtoch.llm.processor.ProcessorType.CHAT_PROCESSOR;
import static ai.knowly.langtoch.llm.processor.ProcessorType.TEXT_PROCESSOR;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.ProcessorType;
import ai.knowly.langtoch.llm.processor.openai.chat.ChatProcessor;
import ai.knowly.langtoch.llm.processor.openai.text.TextProcessor;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.io.input.MultiChatMessageInput;
import ai.knowly.langtoch.llm.schema.io.input.SingleTextInput;
import ai.knowly.langtoch.llm.schema.io.output.SingleTextOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAI contains a set of LLM processors that leverage the OpenAI API to generate response of
 * different types.
 */
public class OpenAI {
  Map<ProcessorType, Processor<?, ?>> supportedProcessors = new HashMap<>();

  private OpenAI() {}

  public static OpenAI create() {
    return new OpenAI();
  }

  public OpenAI withProcessor(ProcessorType processorType, Processor<?, ?> processor) {
    this.supportedProcessors.put(processorType, processor);
    return this;
  }

  public String run(String prompt) {
    // Validate that the processor type is supported.
    validateType(TEXT_PROCESSOR, supportedProcessors);
    TextProcessor processor = (TextProcessor) supportedProcessors.get(TEXT_PROCESSOR);
    SingleTextOutput output = processor.run(SingleTextInput.of(prompt));
    return output.getText();
  }

  public ChatMessage run(List<ChatMessage> chatMessages) {
    // Validate that the processor type is supported.
    validateType(CHAT_PROCESSOR, supportedProcessors);
    ChatProcessor chatProcessor = (ChatProcessor) supportedProcessors.get(CHAT_PROCESSOR);
    return chatProcessor.run(MultiChatMessageInput.of(chatMessages));
  }

  public ChatMessage run(ChatMessage chatMessages) {
    return run(List.of(chatMessages));
  }

  private void validateType(
      ProcessorType processorType, Map<ProcessorType, Processor<?, ?>> processorByType) {
    if (!processorByType.containsKey(processorType)) {
      throw new IllegalArgumentException(
          String.format("No associated processor found: %s", processorType));
    }
    Processor<?, ?> processor = processorByType.get(processorType);
    if (processorType == TEXT_PROCESSOR && processor instanceof TextProcessor) {
      return;
    }
    if (processorType == CHAT_PROCESSOR && processor instanceof ChatProcessor) {
      return;
    }
    throw new IllegalArgumentException(
        String.format("Processor type is not supported: %s", processorType));
  }
}
