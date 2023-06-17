package ai.knowly.langtorch.preprocessing.parser;

import ai.knowly.langtorch.schema.text.SingleText;

/**
 * The SingleTextToStringParser class implements the Parser interface to parse a SingleText object
 * into a String.
 */
public class SingleTextToStringParser implements Parser<SingleText, String> {

  private SingleTextToStringParser() {
    super();
  }

  public static SingleTextToStringParser create() {
    return new SingleTextToStringParser();
  }

  @Override
  public String parse(SingleText input) {
    return input.getText();
  }
}
