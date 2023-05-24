package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
public class SparseValues {
  @JsonProperty("indices")
  private List<Integer> indices;

  @JsonProperty("values")
  private List<Double> values;
}
