package ai.knowly.langtorch.processor.module.minimax.embeddings;

import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.module.EmbeddingsProcessor;
import ai.knowly.langtorch.processor.module.minimax.MiniMaxServiceProvider;
import ai.knowly.langtorch.schema.embeddings.*;
import com.google.common.util.concurrent.ListenableFuture;

public class MiniMaxEmbeddingsProcessor implements EmbeddingsProcessor {
  private final MiniMaxService miniMaxService;

  private MiniMaxEmbeddingsProcessorConfig miniMaxEmbeddingsProcessorConfig =
      MiniMaxEmbeddingsProcessorConfig.builder().build();

  public MiniMaxEmbeddingsProcessor(MiniMaxService miniMaxService) {
    this.miniMaxService = miniMaxService;
  }

  private MiniMaxEmbeddingsProcessor() {
    this.miniMaxService = MiniMaxServiceProvider.createMiniMaxService();
  }

  public static MiniMaxEmbeddingsProcessor create(MiniMaxService MiniMaxService) {
    return new MiniMaxEmbeddingsProcessor(MiniMaxService);
  }

  public static MiniMaxEmbeddingsProcessor create(String groupId, String apiKey) {
    return new MiniMaxEmbeddingsProcessor(
        MiniMaxServiceProvider.createMiniMaxService(groupId, apiKey));
  }

  public static MiniMaxEmbeddingsProcessor create() {
    return new MiniMaxEmbeddingsProcessor();
  }

  public MiniMaxEmbeddingsProcessor withConfig(
      MiniMaxEmbeddingsProcessorConfig MiniMaxEmbeddingsProcessorConfig) {
    this.miniMaxEmbeddingsProcessorConfig = MiniMaxEmbeddingsProcessorConfig;
    return this;
  }

  @Override
  public EmbeddingOutput run(EmbeddingInput inputData) {
    EmbeddingResult embeddingResult =
        miniMaxService.createEmbeddings(
            MiniMaxEmbeddingsProcessorRequestConverter.convert(
                miniMaxEmbeddingsProcessorConfig,
                inputData.getModel(),
                inputData.getInput(),
                "db"));
    return EmbeddingOutput.of(
        EmbeddingType.MINI_MAX,
        embeddingResult.getVectors().stream()
            .map(embedding -> Embedding.ofFloatVector(embedding))
            .collect(toImmutableList()));
  }

  @Override
  public ListenableFuture<EmbeddingOutput> runAsync(EmbeddingInput inputData) {
    return null;
  }
}
