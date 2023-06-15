package ai.knowly.langtorch.capability.modality.text;

import ai.knowly.langtorch.preprocessing.parser.Parser;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Parsers<I, R, S, O> {
  Parser<I, R> inputParser;
  Parser<S, O> outputParser;

  public Optional<Parser<I, R>> getInputParser() {
    return Optional.ofNullable(inputParser);
  }

  public Optional<Parser<S, O>> getOutputParser() {
    return Optional.ofNullable(outputParser);
  }
}
