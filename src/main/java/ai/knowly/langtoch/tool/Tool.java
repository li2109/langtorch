package ai.knowly.langtoch.tool;

import com.google.auto.value.AutoValue;
import java.util.HashMap;
import java.util.Map;

/** A class representing a tool with registered functions. */
@AutoValue
public abstract class Tool {
  public static Builder builder() {
    return new AutoValue_Tool.Builder().setFunctionRegistry(new HashMap<>());
  }

  public abstract String name();

  public abstract String description();

  public abstract Map<String, Function> functionRegistry();

  /**
   * Invoke a registered function with the given label and arguments.
   *
   * @param label the label of the function
   * @param args the arguments to pass to the function
   * @return the result of the function execution
   */
  public Object invoke(String label, Object... args) {
    if (functionRegistry().isEmpty()) {
      throw new IllegalArgumentException("Function registry not found");
    }
    if (functionRegistry().containsKey(label)) {
      return functionRegistry().get(label).execute(args);
    }
    throw new IllegalArgumentException("Function not found");
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String name);

    public abstract Builder setDescription(String description);

    public abstract Builder setFunctionRegistry(Map<String, Function> functionRegistry);

    abstract Map<String, Function> functionRegistry();

    /**
     * Register a new function with the given label.
     *
     * @param label the label of the function
     * @param function the function to register
     * @return the builder with the registered function
     */
    public Builder register(String label, Function function) {
      functionRegistry().put(label, function);
      return this;
    }

    public abstract Tool build();
  }
}