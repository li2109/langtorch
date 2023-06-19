package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.distance;


public interface DistanceStrategy {
    String getSyntax();

    /**
     * Calculates the distance between two vectors based on the specified distance strategy.
     *
     * @param vector1           The first vector.
     * @param vector2           The second vector.
     * @return The calculated distance.
     * @throws IllegalArgumentException if the distance strategy is invalid or the vector dimensions do not match.
     */
    double calculateDistance(double[] vector1, double[] vector2);
}

