package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PineconeDeleteRequest {
  @JsonProperty("ids")
  private List<String> ids;

  @JsonProperty("deleteAll")
  private boolean deleteAll;

  @JsonProperty("namespace")
  private String namespace;

  @JsonProperty("filter")
  private Map<String, String> filter;
}
