package ai.knowly.langtoch.llm.integration.openai.service.schema.completion;

import lombok.Data;

/**
 * A completion generated by OpenAI
 *
 * <p>https://beta.openai.com/docs/api-reference/completions/create
 */
@Data
public class CompletionChoice {
  /** The generated text. Will include the prompt if {@link CompletionRequest#echo } is true */
  String text;

  /** This index of this completion in the returned list. */
  Integer index;

  /**
   * The log probabilities of the chosen tokens and the top {@link CompletionRequest#logprobs}
   * tokens
   */
  LogProbResult logprobs;

  /** The reason why GPT stopped generating, for example "length". */
  String finish_reason;
}