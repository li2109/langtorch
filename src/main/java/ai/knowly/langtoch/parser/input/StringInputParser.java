package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.parser.BaseParser;
import java.util.Map;

/**
 * The common interface for all input parsers with string output.
 *
 * @param <T> The input type of the parser.
 */
public abstract class StringInputParser<T> extends BaseParser<T, String> {
  public abstract Map<Object, Object> getContext();
}
