package ai.knowly.langtorch.schema.embeddings;

import ai.knowly.langtorch.schema.io.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class EmbeddingInput implements Input {
  @Builder.Default private final List<String> input = new ArrayList<>();
  @NonNull private String model;
  private String user;

  public Optional<String> getUser() {
    return Optional.ofNullable(user);
  }
}
