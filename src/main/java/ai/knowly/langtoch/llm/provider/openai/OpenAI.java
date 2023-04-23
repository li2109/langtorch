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
import com.google.common.reflect.TypeToken;
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
    validateType(
        TypeToken.of(String.class), TEXT_PROCESSOR, supportedProcessors.get(TEXT_PROCESSOR));
    TextProcessor processor = (TextProcessor) supportedProcessors.get(TEXT_PROCESSOR);
    SingleTextOutput output = processor.run(SingleTextInput.of(prompt));
    return output.getText();
  }

  public ChatMessage run(List<ChatMessage> chatMessages) {
    // Validate that the processor type is supported.
    validateType(new TypeToken<List<ChatMessage>>() {}, CHAT_PROCESSOR, supportedProcessors.get(CHAT_PROCESSOR));
    ChatProcessor chatProcessor = (ChatProcessor) supportedProcessors.get(CHAT_PROCESSOR);
    return chatProcessor.run(MultiChatMessageInput.of(chatMessages));
  }

  private void validateType(
      TypeToken<?> typeToken, ProcessorType processorType, Processor<?, ?> processor) {
    if (processorType == TEXT_PROCESSOR) {
      if (!(processor instanceof TextProcessor) || !typeToken.isSubtypeOf(String.class)) {
        throw new IllegalArgumentException(
            "The Processor type: " + processorType.name() + " does not support the input type.");
      }
    }
    if (processorType == CHAT_PROCESSOR) {
      if (!(processor instanceof ChatProcessor)) {
        throw new IllegalArgumentException(
            "Processor type: " + processorType.name() + " does not exist.");
      }
    }
  }
}
