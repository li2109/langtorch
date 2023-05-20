package ai.knowly.langtoch.llm.integration.openai.service.schema.dto.completion;

import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.Usage;
import java.util.List;
import lombok.Data;

/**
 * An object containing a response from the completion api
 *
 * <p>https://beta.openai.com/docs/api-reference/completions/create
 */
@Data
public class CompletionResult {
  /** A unique id assigned to this completion. */
  String id;

  /**
   * https://beta.openai.com/docs/api-reference/create-completion The type of object returned,
   * should be "text_completion"
   */
  String object;

  /** The creation time in epoch seconds. */
  long created;

  /** The GPT model used. */
  String model;

  /** A list of generated completions. */
  List<CompletionChoice> choices;

  /** The API usage for this request */
  Usage usage;
}
