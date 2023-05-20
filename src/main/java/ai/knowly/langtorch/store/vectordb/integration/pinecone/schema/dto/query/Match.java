package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.SparseValues;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
  @JsonProperty("id")
  private String id;

  @JsonProperty("score")
  private Double score;

  @JsonProperty("values")
  private List<Double> values;

  @JsonProperty("sparseValues")
  private SparseValues sparseValues;

  @JsonProperty("metadata")
  private Map<String, String> metadata;
}
