package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class FetchRequest {
  @JsonProperty("ids")
  private List<String> ids;

  @JsonProperty("namespace")
  private String namespace;
}
