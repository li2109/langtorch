package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema;


import ai.knowly.langtorch.store.vectordb.integration.pgvector.PGVectorService;
import ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.distance.DistanceStrategy;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.sql.Connection;
import java.util.Optional;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PGVectorStoreSpec {

    @NonNull private final PGVectorService pgVectorService;
    @Builder.Default private final String model = "text-embedding-ada-002";
    @NonNull private final String databaseName;
    private final String textKey;
    private final int vectorDimensions;
    @NonNull private final DistanceStrategy distanceStrategy;

    public Optional<String> getTextKey() {
        return Optional.ofNullable(textKey);
    }

}