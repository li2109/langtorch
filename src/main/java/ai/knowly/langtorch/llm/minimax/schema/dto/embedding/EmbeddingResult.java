package ai.knowly.langtorch.llm.minimax.schema.dto.embedding;

import ai.knowly.langtorch.llm.minimax.schema.dto.BaseResp;
import java.util.List;
import lombok.Data;

/** An object containing a response from the answer api */
@Data
public class EmbeddingResult {

  /** Vector result, one text corresponds to a float32 array, with a length of 1536 */
  private List<List<Float>> vectors;

  private BaseResp baseResp;
}
