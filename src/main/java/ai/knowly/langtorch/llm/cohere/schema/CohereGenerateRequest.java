package ai.knowly.langtorch.llm.cohere.schema;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoValue
public abstract class CohereGenerateRequest {
  private static final String DEFAULT_MODEL = "command";

  public static Builder builder() {
    return new AutoValue_CohereGenerateRequest.Builder()
        .model(DEFAULT_MODEL)
        .numGenerations(1)
        .maxTokens(20)
        .preset("")
        .temperature(0.0)
        .k(0)
        .p(0.0)
        .frequencyPenalty(0.0)
        .presencePenalty(0.0)
        .endSequences(new ArrayList<>())
        .stopSequences(new ArrayList<>())
        .logitBias(new HashMap<>())
        .returnLikelihoods("NONE")
        .truncate("END");
  }

  // Represents the prompt or text to be completed. Trailing whitespaces will be trimmed.
  public abstract String prompt();

  // The size of the model to generate with. Currently available models are command (default),
  // command-nightly (experimental), command-light, and command-light-nightly (experimental).
  // Smaller, "light" models are faster, while larger models will perform better. Custom models can
  // also be supplied with their full ID.
  public abstract String model();

  // Defaults to 1, min value of 1, max value of 5. Denotes the maximum number of generations that
  // will be returned.
  public abstract Integer numGenerations();

  // Denotes the number of tokens to predict per generation, defaults to 20. See BPE
  // Tokens[https://docs.cohere.com/docs/tokens] for more details.
  // Can only be set to 0 if return_likelihoods is set to ALL to get the likelihood of the prompt.
  public abstract Integer maxTokens();

  // The ID of a custom playground preset. You can create presets in the playground. If you use a
  // preset, the prompt parameter becomes optional, and any included parameters will override the
  // preset's parameters.
  public abstract String preset();

  // Min value of 0.0, max value of 5.0. A non-negative float that tunes the
  // degree of randomness in generation. Lower temperatures mean less random generations. See
  // Temperature for more details.
  public abstract Double temperature();

  // Defaults to 0(disabled), which is the minimum. Maximum value is 500. Ensures only the top k
  // most likely tokens are considered for generation at each step.
  public abstract Integer k();

  // Set to 1.0 or 0 to disable. If set to a probability 0.0 < p < 1.0, it ensures
  // that only the most likely tokens, with total probability mass of p, are considered for
  // generation at each step. If both k and p are enabled, p acts after k.
  public abstract Double p();

  // Defaults to 0.0, min value of 0.0, max value of 1.0. Can be used to reduce repetitiveness of
  // generated tokens. The higher the value, the stronger a penalty is applied to previously present
  // tokens, proportional to how many times they have already appeared in the prompt or prior
  // generation.
  public abstract Double frequencyPenalty();

  // Defaults to 0.0, min value of 0.0, max value of 1.0. Can be used to reduce repetitiveness of
  // generated tokens. Similar to frequency_penalty, except that this penalty is applied equally to
  // all tokens that have already appeared, regardless of their exact frequencies.
  public abstract Double presencePenalty();

  // The generated text will be cut at the beginning of the earliest occurrence of an end sequence.
  // The sequence will be excluded from the text.
  public abstract ImmutableList<String> endSequences();

  // The generated text will be cut at the end of the earliest occurence of a stop sequence. The
  // sequence will be included the text.
  public abstract ImmutableList<String> stopSequences();

  // One of GENERATION|ALL|NONE to specify how and if the token likelihoods are returned with the
  // response. Defaults to NONE.
  //
  // If GENERATION is selected, the token likelihoods will only be provided for generated text.
  //
  // If ALL is selected, the token likelihoods will be provided both for the prompt and the
  // generated text.
  public abstract String returnLikelihoods();

  // Used to prevent the model from generating unwanted tokens or to incentivize it to include
  // desired tokens. The format is {token_id: bias} where bias is a float between -10 and 10. Tokens
  // can be obtained from text using Tokenize.
  //
  // For example, if the value {'11': -10} is provided, the model will be very unlikely to include
  // the token 11 ("\n", the newline character) anywhere in the generated text. In contrast {'11':
  // 10} will result in generations that nearly only contain that token. Values between -10 and 10
  // will proportionally affect the likelihood of the token appearing in the generated text.
  //
  // Note: logit bias may not be supported for all custom models.
  public abstract ImmutableMap<String, Float> logitBias();

  // One of NONE|START|END to specify how the API will handle inputs longer than the maximum token
  // length.
  //
  // Passing START will discard the start of the input. END will discard the end of the input. In
  // both cases, input is discarded until the remaining input is exactly the maximum input token
  // length for the model.
  //
  // If NONE is selected, when the input exceeds the maximum input token length an error will be
  // returned.
  public abstract String truncate();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder prompt(String prompt);

    public abstract Builder model(String model);

    public abstract Builder numGenerations(Integer numGenerations);

    public abstract Builder maxTokens(Integer maxTokens);

    public abstract Builder preset(String preset);

    public abstract Builder temperature(Double temperature);

    public abstract Builder k(Integer k);

    public abstract Builder p(Double p);

    public abstract Builder frequencyPenalty(Double frequencyPenalty);

    public abstract Builder presencePenalty(Double presencePenalty);

    public abstract Builder endSequences(List<String> endSequences);

    public abstract Builder stopSequences(List<String> stopSequences);

    public abstract Builder returnLikelihoods(String returnLikelihoods);

    public abstract Builder logitBias(Map<String, Float> logitBias);

    public abstract Builder truncate(String truncate);

    public abstract CohereGenerateRequest build();
  }
}
