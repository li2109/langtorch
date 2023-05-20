package ai.knowly.langtorch.llm.processor.openai.embeddings;

import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.embedding.EmbeddingRequest;
import java.util.List;

public final class OpenAIEmbeddingsProcessorRequestConverter {

  public static EmbeddingRequest convert(
      OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig,
      String model,
      List<String> input) {

    EmbeddingRequest embeddingRequest = new EmbeddingRequest();

    embeddingRequest.setModel(model);
    embeddingRequest.setInput(input);

    openAIEmbeddingsProcessorConfig.getUser().ifPresent(embeddingRequest::setUser);

    return embeddingRequest;
  }
}
