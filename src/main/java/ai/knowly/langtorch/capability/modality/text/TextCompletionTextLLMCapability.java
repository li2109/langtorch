package ai.knowly.langtorch.capability.modality.text;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.processor.Processor;
import ai.knowly.langtorch.store.memory.Memory;
import ai.knowly.langtorch.parser.Parser;
import ai.knowly.langtorch.schema.text.SingleText;
import ai.knowly.langtorch.schema.memory.MemoryKey;
import ai.knowly.langtorch.schema.memory.MemoryValue;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Optional;

public class TextCompletionTextLLMCapability<I, O>
    implements TextLLMCapability<I, SingleText, SingleText, O> {
  private final Processor<SingleText, SingleText> processor;

  private Optional<Parser<I, SingleText>> inputParser;
  private Optional<Parser<SingleText, O>> outputParser;
  private Optional<Memory<MemoryKey, MemoryValue>> memory;

  public TextCompletionTextLLMCapability(Processor<SingleText, SingleText> processor) {
    this.processor = processor;
    this.inputParser = Optional.empty();
    this.outputParser = Optional.empty();
    this.memory = Optional.empty();
  }

  public static <I, O> TextCompletionTextLLMCapability<I, O> of(
      Processor<SingleText, SingleText> processor) {
    return new TextCompletionTextLLMCapability<>(processor);
  }

  public TextCompletionTextLLMCapability<I, O> withInputParser(Parser<I, SingleText> inputParser) {
    this.inputParser = Optional.of(inputParser);
    return this;
  }

  public TextCompletionTextLLMCapability<I, O> withOutputParser(
      Parser<SingleText, O> outputParser) {
    this.outputParser = Optional.of(outputParser);
    return this;
  }

  public TextCompletionTextLLMCapability<I, O> withMemory(Memory<MemoryKey, MemoryValue> memory) {
    this.memory = Optional.of(memory);
    return this;
  }

  @Override
  public SingleText preProcess(I inputData) {
    if (inputData instanceof SingleText) {
      return (SingleText) inputData;
    } else if (inputParser.isPresent()) {
      return inputParser.get().parse(inputData);
    } else {
      throw new IllegalArgumentException(
          "Input data is not a SingleText and no input parser is present.");
    }
  }

  @Override
  public Optional<Memory<MemoryKey, MemoryValue>> getMemory() {
    return this.memory;
  }

  @Override
  public O postProcess(SingleText outputData) {
    if (outputParser.isPresent()) {
      return outputParser.get().parse(outputData);
    } else {
      throw new IllegalArgumentException(
          "Output data type is not SingleText and no output parser is present.");
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
