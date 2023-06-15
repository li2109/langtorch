package ai.knowly.langtorch.processor.module.minimax.embeddings;

import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingRequest;
import java.util.List;

public final class MiniMaxEmbeddingsProcessorRequestConverter {
  private MiniMaxEmbeddingsProcessorRequestConverter() {}

  public static EmbeddingRequest convert(
      MiniMaxEmbeddingsProcessorConfig MiniMaxEmbeddingsProcessorConfig,
      String model,
      List<String> texts,
      String type) {

    EmbeddingRequest embeddingRequest = new EmbeddingRequest();

    embeddingRequest.setModel(model);
    embeddingRequest.setTexts(texts);
    embeddingRequest.setType(type);

    return embeddingRequest;
  }
}
