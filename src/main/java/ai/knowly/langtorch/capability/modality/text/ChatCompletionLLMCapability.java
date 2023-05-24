package ai.knowly.langtorch.capability.modality.text;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.store.memory.Memory;
import ai.knowly.langtorch.parser.Parser;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import ai.knowly.langtorch.schema.memory.MemoryKey;
import ai.knowly.langtorch.schema.memory.MemoryValue;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Optional;

public class ChatCompletionLLMCapability<I, O>
    implements TextLLMCapability<I, MultiChatMessage, ChatMessage, O> {
  private final Processor<MultiChatMessage, ChatMessage> processor;

  private Optional<Parser<I, MultiChatMessage>> inputParser;
  private Optional<Parser<ChatMessage, O>> outputParser;
  private Optional<Memory<MemoryKey, MemoryValue>> memory;

  public ChatCompletionLLMCapability(
      Processor<MultiChatMessage, ChatMessage> processor,
      Optional<Parser<I, MultiChatMessage>> inputParser,
      Optional<Parser<ChatMessage, O>> outputParser) {
    this.processor = processor;
    this.inputParser = inputParser;
    this.outputParser = outputParser;
    this.memory = Optional.empty();
  }

  public static <I, O> ChatCompletionLLMCapability<I, O> of(
      Processor<MultiChatMessage, ChatMessage> processor) {
    return new ChatCompletionLLMCapability<>(processor, Optional.empty(), Optional.empty());
  }

  public ChatCompletionLLMCapability<I, O> withInputParser(
      Parser<I, MultiChatMessage> inputParser) {
    this.inputParser = Optional.of(inputParser);
    return this;
  }

  public ChatCompletionLLMCapability<I, O> withOutputParser(Parser<ChatMessage, O> outputParser) {
    this.outputParser = Optional.of(outputParser);
    return this;
  }

  public ChatCompletionLLMCapability<I, O> withMemory(Memory<MemoryKey, MemoryValue> memory) {
    this.memory = Optional.of(memory);
    return this;
  }

  @Override
  public MultiChatMessage preProcess(I inputData) {
    if (inputData instanceof MultiChatMessage) {
      return (MultiChatMessage) inputData;
    } else if (inputParser.isPresent()) {
      return inputParser.get().parse(inputData);
    } else {
      throw new IllegalArgumentException(
          "Input data is not a MultiChatMessage and no input parser is present.");
    }
  }

  @Override
  public Optional<Memory<MemoryKey, MemoryValue>> getMemory() {
    return this.memory;
  }

  @Override
  public O postProcess(ChatMessage outputData) {
    if (outputParser.isPresent()) {
      return outputParser.get().parse(outputData);
    } else {
      throw new IllegalArgumentException(
          "Output data type is not ChatMessage and no output parser is present.");
    }
  }

  @Override
  public O run(I inputData) {
    return postProcess(processor.run(preProcess(inputData)));
  }

  @Override
  public ListenableFuture<O> runAsync(I inputData) {
    return FluentFuture.from(immediateFuture(inputData)).transform(this::run, directExecutor());
  }
}
