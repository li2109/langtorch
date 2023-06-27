package ai.knowly.langtorch.store.vectordb.integration.pgvector.schema;

import ai.knowly.langtorch.schema.io.Metadata;
import lombok.*;

/** Represents the values of a PGVector. */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, setterPrefix = "set")
public class PGVectorValues {
  @NonNull private final String id;
  private final float @NonNull [] values;
  @NonNull private final Metadata metadata;
}
