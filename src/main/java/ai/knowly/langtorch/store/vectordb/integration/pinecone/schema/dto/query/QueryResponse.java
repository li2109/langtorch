package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
  @JsonProperty("matches")
  private List<Match> matches;

  @JsonProperty("namespace")
  private String namespace;
}
