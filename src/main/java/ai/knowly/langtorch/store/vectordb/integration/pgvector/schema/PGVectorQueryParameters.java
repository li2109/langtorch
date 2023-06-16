package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, setterPrefix = "set")
public class PGVectorQueryParameters {
    @NonNull private final List<PGVectorValues> vectorValues;
    @NonNull private final String vectorParameters;
    @NonNull private final String metadataParameters;
    private final int metadataSize;
}
