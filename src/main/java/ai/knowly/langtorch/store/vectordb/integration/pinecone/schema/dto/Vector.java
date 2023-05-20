package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
public class Vector {
  @JsonProperty("id")
  private String id;

  @JsonProperty("values")
  private List<Double> values;

  @JsonProperty("sparseValues")
  private SparseValues sparseValues;

  @JsonProperty("metadata")
  private Map<String, String> metadata;
}
