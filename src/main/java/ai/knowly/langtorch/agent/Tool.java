package ai.knowly.langtorch.agent;

import java.util.function.Function;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class Tool<T, R> {
  private String name;
  private String description;
  private Function<T, R> function;

  public R invoke(T args) {
    return function.apply(args);
  }
}
