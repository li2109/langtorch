package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.distance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class InnerProductDistanceStrategy implements DistanceStrategy {

  @Override
  public String getSyntax() {
    return "<#>";
  }

  @Override
  public double calculateDistance(double[] vector1, double[] vector2) {
    if (vector1.length != vector2.length) {
      throw new IllegalArgumentException("Vector dimensions do not match.");
    }
    double innerProduct = 0;
    for (int i = 0; i < vector1.length; i++) {
      innerProduct += vector1[i] * vector2[i];
    }
    return innerProduct;
  }
}
