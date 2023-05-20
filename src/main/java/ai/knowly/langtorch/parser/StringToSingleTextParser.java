package ai.knowly.langtorch.parser;

import ai.knowly.langtorch.schema.io.SingleText;

public class StringToSingleTextParser implements Parser<String, SingleText> {

  private StringToSingleTextParser() {
    super();
  }

  public static StringToSingleTextParser create() {
    return new StringToSingleTextParser();
  }

  @Override
  public SingleText parse(String input) {
    return SingleText.of(input);
  }
}
