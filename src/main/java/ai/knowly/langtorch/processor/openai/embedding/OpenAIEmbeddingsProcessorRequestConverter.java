<<<<<<<< HEAD:src/main/java/ai/knowly/langtorch/processor/openai/embeddings/OpenAIEmbeddingsProcessorRequestConverter.java
package ai.knowly.langtorch.processor.openai.embeddings;
========
package ai.knowly.langtorch.processor.module.openai.embedding;
>>>>>>>> upstream/master:src/main/java/ai/knowly/langtorch/processor/openai/embedding/OpenAIEmbeddingsProcessorRequestConverter.java

import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingRequest;
import java.util.List;

public final class OpenAIEmbeddingsProcessorRequestConverter {
  private OpenAIEmbeddingsProcessorRequestConverter() {}

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
