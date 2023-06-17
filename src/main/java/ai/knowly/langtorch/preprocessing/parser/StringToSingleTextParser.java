package ai.knowly.langtorch.preprocessing.parser;

import ai.knowly.langtorch.schema.text.SingleText;

/**
 * The StringToSingleTextParser class is a Java parser that converts a string input into a
 * SingleText object.
 */
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
