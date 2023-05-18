package ai.knowly.langtoch.capability.modality.text;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.memory.Memory;
import ai.knowly.langtoch.parser.Parser;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.io.MultiChatMessage;
import ai.knowly.langtoch.schema.memory.MemoryKey;
import ai.knowly.langtoch.schema.memory.MemoryValue;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ChatCompletionLLMCapability<I, O>
        implements TextLLMCapability<I, MultiChatMessage, ChatMessage, O> {
  private final Processor<MultiChatMessage, ChatMessage> processor;

  private Parser<I, MultiChatMessage> inputParser;
  private Parser<ChatMessage, O> outputParser;
  private Memory<MemoryKey, MemoryValue> memory;

  public ChatCompletionLLMCapability(
          Processor<MultiChatMessage, ChatMessage> processor,
          Parser<I, MultiChatMessage> inputParser,
          Parser<ChatMessage, O> outputParser) {
    this.processor = processor;
    this.inputParser = inputParser;
    this.outputParser = outputParser;
    this.memory = null;
  }

  public static <I, O> ChatCompletionLLMCapability<I, O> of(
      Processor<MultiChatMessage, ChatMessage> processor, Class<O> outputClass) {
    return new ChatCompletionLLMCapability<>(processor, null, null);
  }

  public ChatCompletionLLMCapability<I, O> withInputParser(
      Parser<I, MultiChatMessage> inputParser) {
    this.inputParser = inputParser;
    return this;
  }

  public ChatCompletionLLMCapability<I, O> withOutputParser(Parser<ChatMessage, O> outputParser) {
    this.outputParser = outputParser;
    return this;
  }

  public ChatCompletionLLMCapability<I, O> withMemory(Memory<MemoryKey, MemoryValue> memory) {
    this.memory = memory;
    return this;
  }

  @Override
  public MultiChatMessage preProcess(I inputData) {
    if (inputData instanceof MultiChatMessage) {
      return (MultiChatMessage) inputData;
    }
    return Optional.ofNullable(inputParser)
            .orElseThrow(() -> new IllegalArgumentException("Input data is not a MultiChatMessage and no input parser is present."))
            .parse(inputData);
  }

  @Override
  public Optional<Memory<MemoryKey, MemoryValue>> getMemory() {
    return Optional.ofNullable(this.memory);
  }

  @Override
  public O postProcess(ChatMessage outputData) {
    return Optional.ofNullable(outputParser)
            .orElseThrow(() -> new IllegalArgumentException("Output data type is not ChatMessage and no output parser is present."))
            .parse(outputData);
  }

  @Override
  public O run(I inputData) {
    return postProcess(processor.run(preProcess(inputData)));
  }

  @Override
  public CompletableFuture<O> runAsync(CompletableFuture<I> inputData) {
    return inputData.thenCompose(
        i -> {
          O result = run(i);
          return CompletableFuture.completedFuture(result);
        });
  }
}
