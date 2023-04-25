package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.Input;
import ai.knowly.langtoch.llm.schema.io.Output;
import ai.knowly.langtoch.parser.Parser;
import java.util.Optional;

public abstract class CapabilityUnit<T, I extends Input, R extends Output, U> {
  private Optional<Parser<T, I>> inputParser;
  private Optional<Parser<R, U>> outputParser;
  private Processor<I, R> processor;

  public CapabilityUnit(
      Parser<T, I> inputParser, Parser<R, U> outputParser, Processor<I, R> processor) {
    this.inputParser = Optional.ofNullable(inputParser);
    this.outputParser = Optional.ofNullable(outputParser);
    this.processor = processor;
  }

  public CapabilityUnit(Processor<I, R> processor) {
    this.inputParser = Optional.empty();
    this.outputParser = Optional.empty();
    this.processor = processor;
  }

  public final U run(T input) {
    Optional<I> parsedInput = inputParser.map(parser -> parser.parse(input));
    I processedInput;
    if (parsedInput.isPresent()) {
      processedInput = parsedInput.get();
    } else {
      if (input instanceof Input) {
        processedInput = (I) input;
      } else {
        throw new RuntimeException("Input is not an instance of Input");
      }
    }

    R result = processor.run(processedInput);

    Optional<U> parsedOutput = outputParser.map(parser -> parser.parse(result));
    U finalOutput;
    if (parsedOutput.isPresent()) {
      finalOutput = parsedOutput.get();
    } else {
      if (result != null) {
        finalOutput = (U) result;
      } else {
        throw new RuntimeException("Output is not an instance of Output");
      }
    }

    return finalOutput;
  }

  public CapabilityUnit<T, I, R, U> withOutputParser(Parser<R, U> outputParser) {
    this.outputParser = Optional.of(outputParser);
    return this;
  }

  public CapabilityUnit<T, I, R, U> withInputParser(Parser<T, I> inputParser) {
    this.inputParser = Optional.of(inputParser);
    return this;
  }

  public CapabilityUnit<T, I, R, U> withProcessor(Processor<I, R> processor) {
    this.processor = processor;
    return this;
  }
}
