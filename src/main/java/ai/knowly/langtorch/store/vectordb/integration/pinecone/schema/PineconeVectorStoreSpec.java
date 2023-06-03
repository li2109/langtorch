package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.PineconeService;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PineconeVectorStoreSpec {
  @NonNull private final PineconeService pineconeService;
  private final String namespace;
  private final String textKey;
  @Builder.Default private final String model = "text-embedding-ada-002";

  public Optional<String> getNamespace() {
    return Optional.ofNullable(namespace);
  }

  public Optional<String> getTextKey() {
    return Optional.ofNullable(textKey);
  }
}
