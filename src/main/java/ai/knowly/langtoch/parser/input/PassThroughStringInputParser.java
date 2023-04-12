package ai.knowly.langtoch.parser.input;

import java.util.Map;

public class PassThroughStringInputParser extends BaseStringInputParser<String> {
  @Override
  public String parse(String input) {
    return input;
  }

  @Override
  public Map<Object, Object> getContext() {
    return Map.of();
  }
}
