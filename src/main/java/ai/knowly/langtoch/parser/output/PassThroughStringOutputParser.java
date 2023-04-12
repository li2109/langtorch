package ai.knowly.langtoch.parser.output;

import java.util.Map;

// Just returns the output string as is. This is the default parser.
public final class PassThroughStringOutputParser extends BaseStringOutputParser<String> {

  @Override
  public String parse(String output) {
    return output;
  }

  @Override
  public Map<Object, Object> getContext() {
    return Map.of();
  }
}
