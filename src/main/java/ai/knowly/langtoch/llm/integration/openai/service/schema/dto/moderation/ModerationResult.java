package ai.knowly.langtoch.llm.integration.openai.service.schema.dto.moderation;

import java.util.List;
import lombok.Data;

/**
 * An object containing a response from the moderation api
 *
 * <p>https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationResult {
  /** A unique id assigned to this moderation. */
  public String id;

  /** The model used. */
  public String model;

  /** A list of moderation scores. */
  public List<Moderation> results;
}
