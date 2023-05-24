package ai.knowly.langtorch.parser;

import ai.knowly.langtorch.schema.text.SingleText;

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
