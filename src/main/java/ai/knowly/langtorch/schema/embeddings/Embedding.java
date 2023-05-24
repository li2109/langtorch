package ai.knowly.langtorch.schema.embeddings;

import ai.knowly.langtorch.schema.io.Output;

import java.util.List;

public class Embedding implements Output {

  private final List<Double> vector;

  private Embedding(List<Double> vector) {
    this.vector = vector;
  }

  public static Embedding of(List<Double> vector) {
    return new Embedding(vector);
  }

  public List<Double> getVector() {
    return vector;
  }
}
