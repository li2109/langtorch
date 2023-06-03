package ai.knowly.langtorch.llm.openai.schema.dto.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * An object containing the scores for each moderation category
 *
 * <p>https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationCategoryScores {

  private double hate;

  @JsonProperty("hate/threatening")
  private double hateThreatening;

  @JsonProperty("self-harm")
  private double selfHarm;

  private double sexual;

  @JsonProperty("sexual/minors")
  private double sexualMinors;

  private double violence;

  @JsonProperty("violence/graphic")
  private double violenceGraphic;
}
