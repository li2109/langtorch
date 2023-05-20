package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.SparseValues;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class UpdateRequest {
  @JsonProperty("id")
  @NonNull
  private String id;

  @JsonProperty("values")
  private List<Double> values;

  @JsonProperty("sparseValues")
  private SparseValues sparseValues;

  @JsonProperty("setMetadata")
  private Map<String, String> setMetadata;

  @JsonProperty("namespace")
  private String namespace;
}
