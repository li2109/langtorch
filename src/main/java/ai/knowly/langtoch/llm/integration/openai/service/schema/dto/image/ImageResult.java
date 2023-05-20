package ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image;

import java.util.List;
import lombok.Data;

/**
 * An object with a list of image results.
 *
 * <p>https://beta.openai.com/docs/api-reference/images
 */
@Data
public class ImageResult {

  /** The creation time in epoch seconds. */
  Long created;

  /** List of image results. */
  List<Image> data;
}
