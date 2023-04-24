package ai.knowly.langtoch.parser;

import ai.knowly.langtoch.llm.schema.io.SingleText;

public class StringToSingleTextParser extends Parser<String, SingleText> {

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
