package ai.knowly.langtorch.processor.module.openai.embeddings;

import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtorch.processor.llm.openai.service.OpenAIService;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.module.EmbeddingsOutput;
import ai.knowly.langtorch.processor.module.openai.OpenAIServiceProvider;
import ai.knowly.langtorch.schema.embeddings.Embedding;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingType;
import ai.knowly.langtorch.schema.embeddings.Embeddings;
import com.google.common.util.concurrent.ListenableFuture;

public class OpenAIEmbeddingsOutput implements EmbeddingsOutput {
  private final OpenAIService openAIService;

  private OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig =
      OpenAIEmbeddingsProcessorConfig.builder().build();

  public OpenAIEmbeddingsOutput(OpenAIService openAiApi) {
    this.openAIService = openAiApi;
  }

  private OpenAIEmbeddingsOutput() {
    this.openAIService = OpenAIServiceProvider.createOpenAIService();
  }

  public static OpenAIEmbeddingsOutput create(OpenAIService openAIService) {
    return new OpenAIEmbeddingsOutput(openAIService);
  }

  public static OpenAIEmbeddingsOutput create(String openAIKey) {
    return new OpenAIEmbeddingsOutput(OpenAIServiceProvider.createOpenAIService(openAIKey));
  }

  public static OpenAIEmbeddingsOutput create() {
    return new OpenAIEmbeddingsOutput();
  }

  public OpenAIEmbeddingsOutput withConfig(
      OpenAIEmbeddingsProcessorConfig openAIEmbeddingsProcessorConfig) {
    this.openAIEmbeddingsProcessorConfig = openAIEmbeddingsProcessorConfig;
    return this;
  }

  @Override
  public Embeddings run(EmbeddingInput inputData) {
    EmbeddingResult embeddingResult =
        openAIService.createEmbeddings(
            OpenAIEmbeddingsProcessorRequestConverter.convert(
                openAIEmbeddingsProcessorConfig, inputData.getModel(), inputData.getInput()));
    return Embeddings.of(
        EmbeddingType.OPEN_AI,
        embeddingResult.getData().stream()
            .map(embedding -> Embedding.of(embedding.getValue()))
            .collect(toImmutableList()));
  }

  @Override
  public ListenableFuture<Embeddings> runAsync(EmbeddingInput inputData) {
    return null;
  }
}
