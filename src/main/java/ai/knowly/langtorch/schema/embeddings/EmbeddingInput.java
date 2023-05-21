package ai.knowly.langtorch.schema.embeddings;

import ai.knowly.langtorch.schema.io.Input;
import java.util.List;

public class EmbeddingInput implements Input {

  private final String model;

  private final List<String> input;

  private final String user;

  public EmbeddingInput(String model, List<String> input, String user) {
    this.model = model;
    this.input = input;
    this.user = user;
  }

  public static EmbeddingInput of(String model, List<String> input, String user) {
    return new EmbeddingInput(model, input, user);
  }

  public String getModel() {
    return model;
  }

  public List<String> getInput() {
    return input;
  }

  public String getUser() {
    return user;
  }
}
