package ai.knowly.langtorch.capability.modality.text;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.parser.Parser;
import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import ai.knowly.langtorch.store.memory.Memory;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemoryContext;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Optional;

public class ChatCompletionLLMCapability<I, O>
    implements TextLLMCapabilityWithMemory<
        I, MultiChatMessage, ChatMessage, O, ChatMessage, ConversationMemoryContext> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final Processor<MultiChatMessage, ChatMessage> processor;

  private Optional<Parser<I, MultiChatMessage>> inputParser;
  private Optional<Parser<ChatMessage, O>> outputParser;
  private Optional<Memory<ChatMessage, ConversationMemoryContext>> memory;
  private boolean verbose;

  public ChatCompletionLLMCapability(
      Processor<MultiChatMessage, ChatMessage> processor,
      Optional<Parser<I, MultiChatMessage>> inputParser,
      Optional<Parser<ChatMessage, O>> outputParser) {
    this.processor = processor;
    this.inputParser = inputParser;
    this.outputParser = outputParser;
    this.memory = Optional.empty();
    this.verbose = false;
  }

  public static <I, O> ChatCompletionLLMCapability<I, O> of(
      Processor<MultiChatMessage, ChatMessage> processor) {
    return new ChatCompletionLLMCapability<>(processor, Optional.empty(), Optional.empty());
  }

  protected ChatCompletionLLMCapability<I, O> withInputParser(
      Parser<I, MultiChatMessage> inputParser) {
    this.inputParser = Optional.of(inputParser);
    return this;
  }

  protected ChatCompletionLLMCapability<I, O> withOutputParser(
      Parser<ChatMessage, O> outputParser) {
    this.outputParser = Optional.of(outputParser);
    return this;
  }

  protected ChatCompletionLLMCapability<I, O> withMemory(
      Memory<ChatMessage, ConversationMemoryContext> memory) {
    this.memory = Optional.of(memory);
    return this;
  }

  protected ChatCompletionLLMCapability<I, O> withVerboseMode() {
    this.verbose = true;
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
  public Optional<Memory<ChatMessage, ConversationMemoryContext>> getMemory() {
    return memory;
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
    return postProcess(generateMemorySideEffectResponse(preProcess(inputData)));
  }

  private ChatMessage generateMemorySideEffectResponse(MultiChatMessage multiChatMessage) {
    memory.ifPresent(
        m -> {
          if (verbose) {
            logger.atInfo().log("Memory before processing: %s", m);
          }
        });
    ChatMessage response = processor.run(getMessageWithMemorySideEffect(multiChatMessage));
    // Adding prompt and response.
    memory.ifPresent(m -> multiChatMessage.getMessages().forEach(m::add));
    memory.ifPresent(m -> m.add(response));
    return response;
  }

  private MultiChatMessage getMessageWithMemorySideEffect(MultiChatMessage message) {
    if (!memory.isPresent()) {
      return message;
    }

    // Memory context being empty means that this is the first message in the conversation
    String memoryContext = memory.get().getMemoryContext().get();
    if (memoryContext.isEmpty()) {
      return message;
    }

    MultiChatMessage updatedMessage =
        message.getMessages().stream()
            .map(
                chatMessage ->
                    ChatMessage.of(
                        String.format(
                            "%s%nBelow is my query:%n%s", memoryContext, chatMessage.toString()),
                        chatMessage.getRole()))
            .collect(MultiChatMessage.toMultiChatMessage());

    if (verbose) {
      logger.atInfo().log("Updated Message with Memory Side Effect: %s", updatedMessage);
    }

    return updatedMessage;
  }

  @Override
  public ListenableFuture<O> runAsync(I inputData) {
    return FluentFuture.from(immediateFuture(inputData)).transform(this::run, directExecutor());
  }
}
