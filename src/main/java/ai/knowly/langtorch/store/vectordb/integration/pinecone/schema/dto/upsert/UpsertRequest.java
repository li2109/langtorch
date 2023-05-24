package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class UpsertRequest {
  private List<Vector> vectors;
  private String namespace;
}
