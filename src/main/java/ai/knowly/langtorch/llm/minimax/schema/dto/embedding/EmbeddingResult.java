package ai.knowly.langtorch.llm.minimax.schema.dto.embedding;

import ai.knowly.langtorch.llm.openai.schema.dto.Usage;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.Embedding;
import java.util.List;
import lombok.Data;

/** An object containing a response from the answer api */
@Data
public class EmbeddingResult {

  /** Vector result, one text corresponds to a float32 array, with a length of 1536 */
  private List<List<Float>> vectors;

  /** If the request is incorrect, the corresponding error status code and details */
  private BaseResp baseResp;

  /** Basic response class */
  public class BaseResp {
    /**
     * Status code 1000, unknown error 1001, timeout 1002, triggering current limiting 1004,
     * authentication failure 1013, internal service error 2013, input format information abnormal
     */
    private Long statusCode;

    /** Error details */
    private String statusMsg;
  }
}
