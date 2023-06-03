package ai.knowly.langtorch.schema.io;

import java.util.HashMap;
import java.util.Map;
import lombok.*;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Metadata {
  private static final Metadata DEFAULT_INSTANCE = Metadata.builder().build();
  @Builder.Default private final Map<String, String> value = new HashMap<>();

  public static Metadata getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }
}
