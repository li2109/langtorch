package ai.knowly.langtorch.schema.embeddings;

import static java.util.Collections.emptyList;

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
    return new Embedding(vector, emptyList());
  }

  public static Embedding ofFloatVector(List<Float> floatVector) {
    return new Embedding(emptyList(), floatVector);
  }

  public List<Double> getVector() {
    return vector;
  }

  public List<Float> getFloatVector() {
    return floatVector;
  }
}
