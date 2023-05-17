package ai.knowly.langtoch.llm.processor.openai.image;

import ai.knowly.langtoch.llm.integration.openai.service.schema.image.CreateImageRequest;

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
