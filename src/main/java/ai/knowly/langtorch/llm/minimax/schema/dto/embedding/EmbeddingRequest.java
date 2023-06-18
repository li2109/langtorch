package ai.knowly.langtorch.llm.minimax.schema.dto.embedding;

import java.util.List;
import lombok.*;

/** Creates an embedding vector representing the input text. */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmbeddingRequest {

  /** Requested model, Currently only supported embo-01 */
  private String model;

  /** Text expected to generate vectors */
  private List<String> texts;

  /**
   * The target usage scenario after generating the vector is used to build the vector library, and
   * the generated vector is stored in the library as the retrieved text; db: Used to generate
   * vectors for queries, query: retrieving text
   */
  private String type;
}
