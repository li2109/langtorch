package ai.knowly.langtoch.llm.integration.openai.service.schema.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * An object containing the scores for each moderation category
 *
 * <p>https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationCategoryScores {

  public double hate;

  @JsonProperty("hate/threatening")
  public double hateThreatening;

  @JsonProperty("self-harm")
  public double selfHarm;

  public double sexual;

  @JsonProperty("sexual/minors")
  public double sexualMinors;

  public double violence;

  @JsonProperty("violence/graphic")
  public double violenceGraphic;
}
