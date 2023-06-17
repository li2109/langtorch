package ai.knowly.langtorch.llm.openai.tokenization;

import ai.knowly.langtorch.llm.openai.util.OpenAIModel;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Tokenizer for OpenAI models. It's currently not used as it's provided by OpenAI rest response.
 * Will need this when we support streaming response.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenAITokenizer {
  private static final ImmutableList<OpenAIModel> GPT_3_MODELS =
      ImmutableList.of(
          OpenAIModel.GPT_3_5_TURBO,
          OpenAIModel.GPT_3_5_TURBO_16K,
          OpenAIModel.GPT_3_5_TURBO_0613,
          OpenAIModel.GPT_3_5_TURBO_16K_0613);

  private static final ImmutableList<OpenAIModel> GPT_4_MODELS =
      ImmutableList.of(
          OpenAIModel.GPT_4,
          OpenAIModel.GPT_4_0613,
          OpenAIModel.GPT_4_32K,
          OpenAIModel.GPT_4_32K_0613);

  public static List<Integer> encode(OpenAIModel model, String text) {
    return Objects.requireNonNull(Encodings.ENCODING_BY_MODEL.get(model)).encode(text);
  }

  public static String decode(OpenAIModel model, List<Integer> tokens) {
    return Objects.requireNonNull(Encodings.ENCODING_BY_MODEL.get(model)).decode(tokens);
  }

  public static long getTokenNumber(OpenAIModel model, String text) {
    return encode(model, text).size();
  }

  // The algorithm for counting tokens is based on
  // https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb
  public static long getTokenNumber(OpenAIModel model, List<ChatMessage> messages) {
    int tokensPerMessage = 0;
    int tokensPerName = 0;
    if (GPT_3_MODELS.contains(model)) {
      // Every message follows <|start|>{role/name}\n{content}<|end|>\n
      tokensPerMessage = 4;
      // If there's a name, the role is omitted
      tokensPerName = -1;

    } else if (GPT_4_MODELS.contains(model)) {
      // Every message follows <|start|>{role/name}\n{content}<|end|>\n
      tokensPerMessage = 3;
      // If there's a name, the role is omitted
      tokensPerName = 1;
    } else {
      throw new UnsupportedOperationException("You model is not supported yet for token counting.");
    }

    int numberOfTokens = 0;
    for (ChatMessage message : messages) {
      numberOfTokens += tokensPerMessage;
      numberOfTokens += encode(model, message.getContent()).size();
      numberOfTokens += encode(model, message.getRole().name()).size();
      numberOfTokens += encode(model, message.getName()).size() + tokensPerName;
    }
    // Every reply is primed with <|start|>assistant<|message|>
    numberOfTokens += 3;
    return numberOfTokens;
  }
}
