package ai.knowly.langtoch.tool;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

/** A tool is configured with a set of related functions. */
@Builder(setterPrefix = "set", toBuilder = true)
public final class Tool {
  private String name;
  private String description;
  private Map<String, Function> functionRegistry;

  public void register(String label, Function function) {
    if (functionRegistry == null) {
      functionRegistry = new HashMap<>();
    }
    functionRegistry.put(label, function);
  }

  public Object invoke(String label, Object... args) {
    if (functionRegistry == null) {
      throw new IllegalArgumentException("Function registry not found");
    }
    if (functionRegistry.containsKey(label)) {
      return functionRegistry.get(label).execute(args);
    }
    throw new IllegalArgumentException("Function not found");
  }
}
