package ai.knowly.langtorch.processor.module.openai.image;

import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageRequest.CreateImageRequestBuilder;

public final class OpenAIImageProcessorRequestConverter {
  private OpenAIImageProcessorRequestConverter() {}

  public static CreateImageRequest convert(
      OpenAIImageProcessorConfig openAIImageProcessorConfig, String prompt) {
    CreateImageRequestBuilder createImageRequest = CreateImageRequest.builder().setPrompt(prompt);

    // Set optional configuration properties
    openAIImageProcessorConfig.getN().ifPresent(createImageRequest::setN);
    openAIImageProcessorConfig.getSize().ifPresent(createImageRequest::setSize);
    openAIImageProcessorConfig.getUser().ifPresent(createImageRequest::setUser);

    return createImageRequest.build();
  }
}
