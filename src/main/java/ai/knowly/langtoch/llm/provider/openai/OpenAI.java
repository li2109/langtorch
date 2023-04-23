package ai.knowly.langtoch.llm.provider.openai;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.ProcessorType;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.provider.LargeLanguageModelProvider;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.io.input.Input;
import ai.knowly.langtoch.llm.schema.io.input.MultiChatMessageInput;
import ai.knowly.langtoch.llm.schema.io.input.SingleTextInput;
import ai.knowly.langtoch.llm.schema.io.output.Output;
import ai.knowly.langtoch.llm.schema.io.output.SingleTextOutput;
import java.util.List;

/**
 * OpenAI implementation of the LargeLanguageModelProvider. Adds OpenAI-specific methods and
 * validation logic.
 */
public class OpenAI extends LargeLanguageModelProvider {
  // Private constructor to prevent instantiation
  private OpenAI() {}

  // Factory method to create a new OpenAI instance
  public static OpenAI create() {
    return new OpenAI();
  }

  // Validate that the given processor type and instance are supported by OpenAI
  @Override
  protected void validateType(
      ProcessorType processorType, Processor<? extends Input, ? extends Output> processor) {
    if (processorType == ProcessorType.TEXT_PROCESSOR && processor instanceof OpenAITextProcessor) {
      return;
    }
    if (processorType == ProcessorType.CHAT_PROCESSOR && processor instanceof OpenAIChatProcessor) {
      return;
    }
    throw new IllegalArgumentException(
        String.format("Processor type is not supported: %s", processorType));
  }

  // Override the withProcessor method to return an OpenAI instance
  @Override
  public OpenAI withProcessor(
      ProcessorType processorType, Processor<? extends Input, ? extends Output> processor) {
    super.withProcessor(processorType, processor);
    return this;
  }

  // Convenience method to run a text processor with a given prompt and return the output text
  public String runTextProcessor(String prompt) {
    SingleTextOutput output = run(SingleTextInput.of(prompt), ProcessorType.TEXT_PROCESSOR);
    return output.getText();
  }

  // Convenience method to run a chat processor with a list of chat messages and return the output
  // chat message
  public ChatMessage runChatProcessor(List<ChatMessage> chatMessages) {
    return run(MultiChatMessageInput.of(chatMessages), ProcessorType.CHAT_PROCESSOR);
  }

  // Convenience method to run a chat processor with a single chat message and return the output
  // chat message
  public ChatMessage runChatProcessor(ChatMessage chatMessage) {
    return runChatProcessor(List.of(chatMessage));
  }
}
