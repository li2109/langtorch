package ai.knowly.langtoch.llm.processor.openai.image;

import static ai.knowly.langtoch.llm.Utils.singleToCompletableFuture;
import static com.google.common.collect.ImmutableList.toImmutableList;

import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtoch.schema.image.Image;
import ai.knowly.langtoch.schema.image.Images;
import ai.knowly.langtoch.schema.io.SingleText;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.OpenAiApi;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OpenAIImageProcessor implements Processor<SingleText, Images> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  // OpenAiApi instance used for making requests
  private final OpenAiApi openAiApi;

  // Configuration for the OpenAI Image Processor
  private OpenAIImageProcessorConfig openAIImageProcessorConfig =
      OpenAIImageProcessorConfig.builder().build();

  // Constructor with dependency injection
  OpenAIImageProcessor(OpenAiApi openAiApi) {
    this.openAiApi = openAiApi;
  }

  // Private constructor used in factory methods
  private OpenAIImageProcessor() {
    this.openAiApi = OpenAIServiceProvider.createOpenAiAPI();
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAIImageProcessor create(OpenAiApi openAiApi) {
    return new OpenAIImageProcessor(openAiApi);
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAIImageProcessor create(String openAIKey) {
    return new OpenAIImageProcessor(OpenAIServiceProvider.createOpenAiAPI(openAIKey));
  }

  // Factory method to create a new OpenAITextProcessor instance
  public static OpenAIImageProcessor create() {
    return new OpenAIImageProcessor();
  }

  // Method to set the processor configuration
  public OpenAIImageProcessor withConfig(OpenAIImageProcessorConfig openAIImageProcessorConfig) {
    this.openAIImageProcessorConfig = openAIImageProcessorConfig;
    return this;
  }

  // Method to run the processor with the given input and return the output text
  @Override
  public Images run(SingleText inputData) {
    try {
      return runAsync(CompletableFuture.completedFuture(inputData)).get();
    } catch (InterruptedException | ExecutionException e) {
      logger.atWarning().withCause(e).log("Error running OpenAI Text Processor");
      throw new RuntimeException(e);
    }
  }

  @Override
  public CompletableFuture<Images> runAsync(CompletableFuture<SingleText> inputData) {
    return inputData.thenCompose(
        data ->
            singleToCompletableFuture(
                    openAiApi.createImage(
                        OpenAIImageProcessorRequestConverter.convert(
                            openAIImageProcessorConfig, data.getText())))
                .thenApply(
                    result ->
                        Images.of(
                            result.getCreated(),
                            result.getData().stream()
                                .map(image -> Image.of(image.getUrl()))
                                .collect(toImmutableList()))));
  }
}
