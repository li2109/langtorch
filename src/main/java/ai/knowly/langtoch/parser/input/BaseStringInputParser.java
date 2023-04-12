package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.parser.BaseParser;
import java.util.Map;

public abstract class BaseStringInputParser<T> extends BaseParser<T, String> {
  public abstract Map<Object, Object> getContext();
}
