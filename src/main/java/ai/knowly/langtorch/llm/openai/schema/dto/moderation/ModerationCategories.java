package ai.knowly.langtorch.llm.openai.schema.dto.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * An object containing the flags for each moderation category
 *
 * <p>https://beta.openai.com/docs/api-reference/moderations/create
 */
@Data
public class ModerationCategories {

  private boolean hate;

  @JsonProperty("hate/threatening")
  private boolean hateThreatening;

  @JsonProperty("self-harm")
  private boolean selfHarm;

  private boolean sexual;

  @JsonProperty("sexual/minors")
  private boolean sexualMinors;

  private boolean violence;

  @JsonProperty("violence/graphic")
  private boolean violenceGraphic;
}
