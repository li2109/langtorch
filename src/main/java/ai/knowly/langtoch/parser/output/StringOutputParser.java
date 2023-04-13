package ai.knowly.langtoch.parser.output;

import ai.knowly.langtoch.parser.BaseParser;
import java.util.Map;

/**
 * The common interface for all output parsers with string input.
 *
 * @param <R> The output type of the parser.
 */
public abstract class StringOutputParser<R> extends BaseParser<String, R> {
  public abstract Map<Object, Object> getContext();
}
