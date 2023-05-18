package ai.knowly.langtoch.capability.modality.text;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.memory.Memory;
import ai.knowly.langtoch.parser.Parser;
import ai.knowly.langtoch.schema.io.SingleText;
import ai.knowly.langtoch.schema.memory.MemoryKey;
import ai.knowly.langtoch.schema.memory.MemoryValue;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TextCompletionTextLLMCapability<I, O>
        implements TextLLMCapability<I, SingleText, SingleText, O> {
  private final Processor<SingleText, SingleText> processor;

  private Parser<I, SingleText> inputParser;
  private Parser<SingleText, O> outputParser;
  private Memory<MemoryKey, MemoryValue> memory;

  public TextCompletionTextLLMCapability(Processor<SingleText, SingleText> processor) {
    this.processor = processor;
    this.inputParser = null;
    this.outputParser = null;
    this.memory = null;
  }

  public static <I, O> TextCompletionTextLLMCapability<I, O> of(
          Processor<SingleText, SingleText> processor) {
    return new TextCompletionTextLLMCapability<>(processor);
  }

  public TextCompletionTextLLMCapability<I, O> withInputParser(Parser<I, SingleText> inputParser) {
    this.inputParser = inputParser;
    return this;
  }

  public TextCompletionTextLLMCapability<I, O> withOutputParser(
      Parser<SingleText, O> outputParser) {
    this.outputParser = outputParser;
    return this;
  }

  public TextCompletionTextLLMCapability<I, O> withMemory(Memory<MemoryKey, MemoryValue> memory) {
    this.memory = memory;
    return this;
  }

  @Override
  public SingleText preProcess(I inputData) {
    if (inputData instanceof SingleText) {
      return (SingleText) inputData;
    }
    return Optional.ofNullable(inputParser)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Input data is not a SingleText and no input parser is present."))
            .parse(inputData);
  }

  @Override
  public Optional<Memory<MemoryKey, MemoryValue>> getMemory() {
    return Optional.ofNullable(this.memory);
  }

  @Override
  public O postProcess(SingleText outputData) {
    return Optional.ofNullable(outputParser).orElseThrow(() -> new IllegalArgumentException(
                    "Output data type is not SingleText and no output parser is present."))
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
