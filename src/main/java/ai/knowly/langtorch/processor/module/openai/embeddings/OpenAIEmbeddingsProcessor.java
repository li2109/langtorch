package ai.knowly.langtorch.processor.module.openai.embeddings;

import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.module.EmbeddingsProcessor;
import ai.knowly.langtorch.processor.module.openai.OpenAIServiceProvider;
import ai.knowly.langtorch.schema.embeddings.Embedding;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingType;
import com.google.common.util.concurrent.ListenableFuture;

public class OpenAIEmbeddingsProcessor implements EmbeddingsProcessor {
  private final OpenAIService openAIService;

  private OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig =
      OpenAIEmbeddingsProcessorConfig.builder().build();

  public OpenAIEmbeddingsProcessor(OpenAIService openAiApi) {
    this.openAIService = openAiApi;
  }

  private OpenAIEmbeddingsProcessor() {
    this.openAIService = OpenAIServiceProvider.createOpenAIService();
  }

  public static OpenAIEmbeddingsProcessor create(OpenAIService openAIService) {
    return new OpenAIEmbeddingsProcessor(openAIService);
  }

  public static OpenAIEmbeddingsProcessor create(String openAIKey) {
    return new OpenAIEmbeddingsProcessor(OpenAIServiceProvider.createOpenAIService(openAIKey));
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
    return null;
  }
}
