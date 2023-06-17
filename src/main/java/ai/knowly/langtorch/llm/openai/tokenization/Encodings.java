package ai.knowly.langtorch.llm.openai.tokenization;

import ai.knowly.langtorch.llm.openai.util.OpenAIModel;
import com.google.common.collect.ImmutableMap;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The class Encodings contains a static map of OpenAI models and their corresponding encodings
 * obtained from a default encoding registry.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Encodings {
  private static final EncodingRegistry registry =
      com.knuddels.jtokkit.Encodings.newDefaultEncodingRegistry();
  public static final ImmutableMap<OpenAIModel, Encoding> ENCODING_BY_MODEL =
      ImmutableMap.of(
          OpenAIModel.GPT_3_5_TURBO,
          registry.getEncodingForModel(ModelType.GPT_3_5_TURBO),
          OpenAIModel.GPT_3_5_TURBO_16K,
          registry.getEncodingForModel(ModelType.GPT_3_5_TURBO),
          OpenAIModel.GPT_3_5_TURBO_0613,
          registry.getEncodingForModel(ModelType.GPT_3_5_TURBO),
          OpenAIModel.GPT_3_5_TURBO_16K_0613,
          registry.getEncodingForModel(ModelType.GPT_3_5_TURBO),
          OpenAIModel.GPT_4,
          registry.getEncodingForModel(ModelType.GPT_4),
          OpenAIModel.GPT_4_0613,
          registry.getEncodingForModel(ModelType.GPT_4),
          OpenAIModel.GPT_4_32K,
          registry.getEncodingForModel(ModelType.GPT_4_32K),
          OpenAIModel.GPT_4_32K_0613,
          registry.getEncodingForModel(ModelType.GPT_4_32K));
}
