package ai.knowly.langtoch.parser.output;

import ai.knowly.langtoch.parser.BaseParser;
import java.util.Map;

/** The common interface for all output parsers with string input. */
public abstract class BaseStringOutputParser<R> extends BaseParser<String, R> {
  public abstract Map<Object, Object> getContext();
}
