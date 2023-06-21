package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;

/**
 * Represents the specification for a PGVector store.
 */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PGVectorStoreSpec {

    @Builder.Default private final String model = "text-embedding-ada-002";
    @NonNull private final String databaseName;
    private final String textKey;
    private final int vectorDimensions;
    private final boolean overwriteExistingTables;
    public Optional<String> getTextKey() {
        return Optional.ofNullable(textKey);
    }

}