package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.parser.BaseParser;

public abstract class BaseStringInputParser<T> extends BaseParser<T, String> {
  public abstract String parse(T input);
}
