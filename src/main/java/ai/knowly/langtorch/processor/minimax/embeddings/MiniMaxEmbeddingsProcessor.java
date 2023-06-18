package ai.knowly.langtorch.processor.minimax.embeddings;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.EmbeddingProcessor;
import ai.knowly.langtorch.schema.embeddings.*;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class MiniMaxEmbeddingsProcessor implements EmbeddingProcessor {

  private final MiniMaxService miniMaxService;

  private final MiniMaxEmbeddingsProcessorConfig miniMaxEmbeddingsProcessorConfig;

  public MiniMaxEmbeddingsProcessor(
      MiniMaxService miniMaxService,
      MiniMaxEmbeddingsProcessorConfig miniMaxEmbeddingsProcessorConfig) {
    this.miniMaxService = miniMaxService;
    this.miniMaxEmbeddingsProcessorConfig = miniMaxEmbeddingsProcessorConfig;
  }

  @Override
  public EmbeddingOutput run(EmbeddingInput inputData) {
    EmbeddingResult embeddingResult =
        miniMaxService.createEmbeddings(
            MiniMaxEmbeddingsProcessorRequestConverter.convert(
                inputData.getModel(),
                inputData.getInput(),
                MiniMaxEmbeddingTypeScene.DB.toString()));
    return EmbeddingOutput.of(
        EmbeddingType.MINI_MAX,
        embeddingResult.getVectors().stream()
            .map(Embedding::ofFloatVector)
            .collect(toImmutableList()));
  }

  @Override
  public ListenableFuture<EmbeddingOutput> runAsync(EmbeddingInput inputData) {
    ListenableFuture<EmbeddingResult> embeddingResult =
        miniMaxService.createEmbeddingsAsync(
            MiniMaxEmbeddingsProcessorRequestConverter.convert(
                inputData.getModel(),
                inputData.getInput(),
                MiniMaxEmbeddingTypeScene.DB.toString()));

    return Futures.transform(
        embeddingResult,
        result -> {
          miniMaxService.checkResp(result.getBaseResp());
          return EmbeddingOutput.of(
              EmbeddingType.MINI_MAX,
              result.getVectors().stream()
                  .map(Embedding::ofFloatVector)
                  .collect(toImmutableList()));
        },
        directExecutor());
  }
}
