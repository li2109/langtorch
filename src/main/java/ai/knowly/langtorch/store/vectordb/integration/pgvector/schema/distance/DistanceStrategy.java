package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.distance;

public interface DistanceStrategy {
    String getValue();
    double calculateDistance(double[] vector1, double[] vector2);

    static DistanceStrategy euclidean() {
        return new EuclideanDistanceStrategy();
    }

    static DistanceStrategy innerProduct() {
        return new InnerProductDistanceStrategy();
    }

    static DistanceStrategy cosine() {
        return new CosineDistanceStrategy();
    }

}
