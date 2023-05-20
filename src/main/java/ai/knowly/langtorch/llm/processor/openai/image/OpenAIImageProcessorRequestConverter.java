package ai.knowly.langtorch.llm.processor.openai.image;

import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.image.CreateImageRequest;

public final class OpenAIImageProcessorRequestConverter {
  public static CreateImageRequest convert(
      OpenAIImageProcessorConfig openAIImageProcessorConfig, String prompt) {
    CreateImageRequest createImageRequest = new CreateImageRequest();

    // Set required configuration properties
    createImageRequest.setPrompt(prompt);

    // Set optional configuration properties
    openAIImageProcessorConfig.getN().ifPresent(createImageRequest::setN);
    openAIImageProcessorConfig.getSize().ifPresent(createImageRequest::setSize);
    openAIImageProcessorConfig.getUser().ifPresent(createImageRequest::setUser);

    return createImageRequest;
  }
}
