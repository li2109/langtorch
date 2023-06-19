package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.distance;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to get instances for vector distance calculating strategies.
 */
public class DistanceStrategies {

    @NonNull
    public static DistanceStrategy euclidean() {
        return new EuclideanDistanceStrategy();
    }

    @NotNull
    public static DistanceStrategy innerProduct() {
        return new InnerProductDistanceStrategy();
    }

    @NotNull
    public static DistanceStrategy cosine() {
        return new CosineDistanceStrategy();
    }
}
