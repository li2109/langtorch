package ai.knowly.langtoch.capability.unit;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.schema.io.input.Input;
import ai.knowly.langtoch.llm.schema.io.output.Output;
import ai.knowly.langtoch.parser.BaseParser;
import java.util.Optional;

public class NewCapabilityUnit<T, I extends Input, R extends Output, U>
    extends BaseCapabilityUnit<I, R> {
  private Optional<BaseParser<T, I>> inputParser = Optional.empty();
  private Optional<BaseParser<R, U>> outputParser = Optional.empty();

  public NewCapabilityUnit(Processor<I, R> processor) {
    super(processor);
  }

  public static <T, I extends Input, R extends Output, U> NewCapabilityUnit<T, I, R, U> with(
      Processor<I, R> processor) {
    return new NewCapabilityUnit<>(processor);
  }

  public NewCapabilityUnit<T, I, R, U> withInputParser(BaseParser<T, I> inputParser) {
    this.inputParser = Optional.of(inputParser);
    return this;
  }

  public NewCapabilityUnit<T, I, R, U> withOutputParser(BaseParser<R, U> outputParser) {
    this.outputParser = Optional.of(outputParser);
    return this;
  }

  @SuppressWarnings("unchecked")
  public U run(T input) {
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
