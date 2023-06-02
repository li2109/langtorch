package ai.knowly.langtorch.llm.openai.schema.dto.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents an embedding returned by the embedding api
 *
 * <p>https://beta.openai.com/docs/api-reference/classifications/create
 */
@Data
public class Embedding {

  /** The type of object returned, should be "embedding" */
  String object;

  /** The embedding vector */
  @JsonProperty("embedding")
  List<Double> value;

  /** The position of this embedding in the list */
  Integer index;
}
