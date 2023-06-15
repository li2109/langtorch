package ai.knowly.langtorch.schema.embeddings;

import ai.knowly.langtorch.schema.io.Output;

import java.util.List;

public class Embedding implements Output {

  private final List<Double> vector;

  private final List<Float> floatVector;

  private Embedding(List<Double> vector, List<Float> floatVector) {
    this.vector = vector;
    this.floatVector = floatVector;
  }

  public static Embedding of(List<Double> vector) {
    return new Embedding(vector, null);
  }

  public static Embedding ofFloatVector(List<Float> floatVector) {
    return new Embedding(null, floatVector);
  }

  public List<Double> getVector() {
    return vector;
  }

  public List<Float> getFloatVector() {
    return floatVector;
  }
}
