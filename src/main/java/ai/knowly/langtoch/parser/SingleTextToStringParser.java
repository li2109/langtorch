package ai.knowly.langtoch.parser;

import ai.knowly.langtoch.llm.schema.io.SingleText;

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
