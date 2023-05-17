package ai.knowly.langtoch.llm.processor.openai.embeddings;

import static ai.knowly.langtoch.llm.Utils.singleToCompletableFuture;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAiApi;
import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtoch.schema.embeddings.Embedding;
import ai.knowly.langtoch.schema.embeddings.EmbeddingType;
import ai.knowly.langtoch.schema.embeddings.Embeddings;
import ai.knowly.langtoch.schema.io.EmbeddingInput;
import com.google.common.flogger.FluentLogger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OpenAIEmbeddingsProcessor implements Processor<EmbeddingInput, Embeddings> {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final OpenAiApi openAiApi;

  private OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig =
      OpenAIEmbeddingsProcessorConfig.builder().build();

  public OpenAIEmbeddingsProcessor(OpenAiApi openAiApi) {
    this.openAiApi = openAiApi;
  }

  private OpenAIEmbeddingsProcessor() {
    this.openAiApi = OpenAIServiceProvider.createOpenAiAPI();
  }

  public static OpenAIEmbeddingsProcessor create(OpenAiApi openAiApi) {
    return new OpenAIEmbeddingsProcessor(openAiApi);
  }

  public static OpenAIEmbeddingsProcessor create(String openAIKey) {
    return new OpenAIEmbeddingsProcessor(OpenAIServiceProvider.createOpenAiAPI(openAIKey));
  }

  public static OpenAIEmbeddingsProcessor create() {
    return new OpenAIEmbeddingsProcessor();
  }

  public OpenAIEmbeddingsProcessor withConfig(
      OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig) {
    this.openAIEmbeddingsProcessorConfig = openAIEmbeddingsProcessorConfig;
    return this;
  }

  @Override
  public Embeddings run(EmbeddingInput inputData) {
    try {
      return runAsync(CompletableFuture.completedFuture(inputData)).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.atWarning().withCause(e).log("Error running OpenAI Embedding Processor");
      throw new RuntimeException(e);
    }
  }

  @Override
  public CompletableFuture<Embeddings> runAsync(CompletableFuture<EmbeddingInput> inputData) {
    return inputData.thenCompose(
        data ->
            singleToCompletableFuture(
                    openAiApi.createEmbeddings(
                        OpenAIEmbeddingsProcessorRequestConverter.convert(
                            openAIEmbeddingsProcessorConfig, data.getModel(), data.getInput())))
                .thenApply(
                    result ->
                        Embeddings.of(
                            EmbeddingType.OPEN_AI,
                            result.getData().stream()
                                .map(embedding -> Embedding.of(embedding.getEmbedding()))
                                .collect(toImmutableList()))));
  }
}
