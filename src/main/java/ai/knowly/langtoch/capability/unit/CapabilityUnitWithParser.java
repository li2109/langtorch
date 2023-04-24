package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.Input;
import ai.knowly.langtoch.llm.schema.io.Output;
import ai.knowly.langtoch.parser.Parser;
import java.util.Optional;

public class CapabilityUnitWithParser<T, I extends Input, R extends Output, U>
    extends CapabilityUnit<I, R> {
  private Optional<Parser<T, I>> inputParser = Optional.empty();
  private Optional<Parser<R, U>> outputParser = Optional.empty();

  public CapabilityUnitWithParser(Processor<I, R> processor) {
    super(processor);
  }

  protected static <T, I extends Input, R extends Output, U>
      CapabilityUnitWithParser<T, I, R, U> with(Processor<I, R> processor) {
    return new CapabilityUnitWithParser<>(processor);
  }

  public final CapabilityUnitWithParser<T, I, R, U> withInputParser(Parser<T, I> inputParser) {
    this.inputParser = Optional.of(inputParser);
    return this;
  }

  public final CapabilityUnitWithParser<T, I, R, U> withOutputParser(Parser<R, U> outputParser) {
    this.outputParser = Optional.of(outputParser);
    return this;
  }

  @SuppressWarnings("unchecked")
  protected U run(T input) {
    I processedInput =
        inputParser
            .map(parser -> parser.parse(input))
            .orElseThrow(() -> new IllegalArgumentException("Input parser is not provided."));

    R result = super.run(processedInput);

    return outputParser
        .map(parser -> parser.parse(result))
        .orElseThrow(() -> new IllegalArgumentException("Output parser is not provided."));
  }
}
