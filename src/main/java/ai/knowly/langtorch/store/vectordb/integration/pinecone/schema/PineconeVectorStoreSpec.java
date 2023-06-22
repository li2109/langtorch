package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PineconeVectorStoreSpec {
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
