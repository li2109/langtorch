package ai.knowly.langtorch.processor.module.openai.image;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.processor.llm.openai.service.OpenAIService;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.image.CreateImageRequest;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.image.ImageResult;
import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.processor.module.openai.OpenAIServiceProvider;
import ai.knowly.langtorch.schema.image.Image;
import ai.knowly.langtorch.schema.image.Images;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;

public class OpenAIImageProcessor implements Processor<SingleText, Images> {
  private final OpenAIService openAIService;

  // Configuration for the OpenAI Image Processor
  private OpenAIImageProcessorConfig openAIImageProcessorConfig =
      OpenAIImageProcessorConfig.builder().build();

  // Constructor with dependency injection
  OpenAIImageProcessor(OpenAIService openAIService) {
    this.openAIService = openAIService;
  }

  // Private constructor used in factory methods
  private OpenAIImageProcessor() {
    this.openAIService = OpenAIServiceProvider.createOpenAIService();
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAIImageProcessor create(OpenAIService openAiApi) {
    return new OpenAIImageProcessor(openAiApi);
  }

  // Factory method to create a new OpenAITextProcessor instance with a given OpenAiService instance
  public static OpenAIImageProcessor create(String openAIKey) {
    return new OpenAIImageProcessor(OpenAIServiceProvider.createOpenAIService(openAIKey));
  }

  // Factory method to create a new OpenAITextProcessor instance
  public static OpenAIImageProcessor create() {
    return new OpenAIImageProcessor();
  }

  // Method to set the module configuration
  public OpenAIImageProcessor withConfig(OpenAIImageProcessorConfig openAIImageProcessorConfig) {
    this.openAIImageProcessorConfig = openAIImageProcessorConfig;
    return this;
  }

  // Method to run the module with the given input and return the output text
  @Override
  public Images run(SingleText inputData) {
    CreateImageRequest createImageRequest =
        OpenAIImageProcessorRequestConverter.convert(
            openAIImageProcessorConfig, inputData.getText());
    ImageResult result = openAIService.createImage(createImageRequest);
    return Images.of(
        result.getCreated(),
        result.getData().stream()
            .map(image -> Image.of(image.getUrl()))
            .collect(toImmutableList()));
  }

  @Override
  public ListenableFuture<Images> runAsync(SingleText inputData) {
    CreateImageRequest createImageRequest =
        OpenAIImageProcessorRequestConverter.convert(
            openAIImageProcessorConfig, inputData.getText());
    return FluentFuture.from(openAIService.createImageAsync(createImageRequest))
        .transform(
            result ->
                Images.of(
                    result.getCreated(),
                    result.getData().stream()
                        .map(image -> Image.of(image.getUrl()))
                        .collect(toImmutableList())),
            directExecutor());
  }
}
