package ai.knowly.langtoch.llm.integration.openai.service.schema.embedding;

import ai.knowly.langtoch.llm.integration.openai.service.schema.Usage;
import java.util.List;
import lombok.Data;

/**
 * An object containing a response from the answer api
 *
 * <p>https://beta.openai.com/docs/api-reference/embeddings/create
 */
@Data
public class EmbeddingResult {

  /** The GPTmodel used for generating embeddings */
  String model;

  /** The type of object returned, should be "list" */
  String object;

  /** A list of the calculated embeddings */
  List<Embedding> data;

  /** The API usage for this request */
  Usage usage;
}
