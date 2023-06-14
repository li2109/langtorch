package ai.knowly.langtorch.processor.module.openai.image;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.image.ImageResult;
import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.schema.image.Image;
import ai.knowly.langtorch.schema.image.Images;
import ai.knowly.langtorch.schema.text.SingleText;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.ListenableFuture;
import javax.inject.Inject;

public class OpenAIImageProcessor implements Processor<SingleText, Images> {
  private final OpenAIService openAIService;

  private final OpenAIImageProcessorConfig openAIImageProcessorConfig;

  @Inject
  public OpenAIImageProcessor(
      OpenAIService openAIService, OpenAIImageProcessorConfig openAIImageProcessorConfig) {
    this.openAIService = openAIService;
    this.openAIImageProcessorConfig = openAIImageProcessorConfig;
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
