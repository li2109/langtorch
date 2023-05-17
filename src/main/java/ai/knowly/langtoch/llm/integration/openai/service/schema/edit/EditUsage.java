package ai.knowly.langtoch.llm.integration.openai.service.schema.edit;

import lombok.Data;

/**
 * An object containing the API usage for an edit request
 *
 * <p>Deprecated, use {@link com.theokanning.openai.Usage} instead
 *
 * <p>https://beta.openai.com/docs/api-reference/edits/create
 */
@Data
@Deprecated
public class EditUsage {

  /** The number of prompt tokens consumed. */
  String promptTokens;

  /** The number of completion tokens consumed. */
  String completionTokens;

  /** The number of total tokens consumed. */
  String totalTokens;
}
