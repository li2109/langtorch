package ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.SparseValues;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PineconeQueryRequest {
  private String namespace;
  private long topK;
  // The filter to apply. You can use vector metadata to limit your search. See
  // https://www.pinecone.io/docs/metadata-filtering/.
  private Map<String, String> filter;
  private boolean includeValues;
  private boolean includeMetadata;
  private List<Double> vector;
  private SparseValues sparseVector;
  private String id;
}
