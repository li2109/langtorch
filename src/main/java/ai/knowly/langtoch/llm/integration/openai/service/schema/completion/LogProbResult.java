package ai.knowly.langtoch.llm.integration.openai.service.schema.completion;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * Log probabilities of different token options Returned if {@link CompletionRequest#logprobs} is
 * greater than zero
 *
 * <p>https://beta.openai.com/docs/api-reference/create-completion
 */
@Data
public class LogProbResult {

  /** The tokens chosen by the completion api */
  List<String> tokens;

  /** The log probability of each token in {@link tokens} */
  @JsonProperty("token_logprobs")
  List<Double> tokenLogprobs;

  /**
   * A map for each index in the completion result. The map contains the top {@link
   * CompletionRequest#logprobs} tokens and their probabilities
   */
  @JsonProperty("top_logprobs")
  List<Map<String, Double>> topLogprobs;

  /** The character offset from the start of the returned text for each of the chosen tokens. */
  List<Integer> textOffset;
}
