package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PineconeUpsertResponse {
  @JsonProperty("upsertedCount")
  long upsertedCount;
}
