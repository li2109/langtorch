package ai.knowly.langtorch.processor.module.openai.embedding;

import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.module.EmbeddingProcessor;
import ai.knowly.langtorch.schema.embeddings.Embedding;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingType;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

/** Embeddings processor for OpenAI. */
public class OpenAIEmbeddingProcessor implements EmbeddingProcessor {
  private final OpenAIService openAIService;
  private final OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig;

  @Inject
  public OpenAIEmbeddingProcessor(
      OpenAIService openAiApi, OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig) {
    this.openAIService = openAiApi;
    this.openAIEmbeddingsProcessorConfig = openAIEmbeddingsProcessorConfig;
  }

  @Override
  public EmbeddingOutput run(EmbeddingInput inputData) {
    EmbeddingResult embeddingResult =
        openAIService.createEmbeddings(
            OpenAIEmbeddingsProcessorRequestConverter.convert(
                openAIEmbeddingsProcessorConfig, inputData.getModel(), inputData.getInput()));
    return EmbeddingOutput.of(
        EmbeddingType.OPEN_AI,
        embeddingResult.getData().stream()
            .map(embedding -> Embedding.of(embedding.getValue()))
            .collect(toImmutableList()));
  }

  @Override
  public ListenableFuture<EmbeddingOutput> runAsync(EmbeddingInput inputData) {
    // TODO: Implement async
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
