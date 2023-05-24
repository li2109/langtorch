package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchResponse {
  @JsonProperty("vectors")
  private Map<String, Vector> vectors;

  @JsonProperty("namespace")
  private String namespace;
}
