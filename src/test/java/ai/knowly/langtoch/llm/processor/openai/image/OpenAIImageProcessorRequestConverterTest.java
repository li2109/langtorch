package ai.knowly.langtoch.llm.processor.openai.image;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.image.CreateImageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class OpenAIImageProcessorRequestConverterTest {
  private OpenAIImageProcessorConfig openAIImageProcessorConfig;

  @BeforeEach
  void setUp() {
    openAIImageProcessorConfig =
        OpenAIImageProcessorConfig.builder().setN(5).setSize("512x512").setUser("user123").build();
  }

  @Test
  void testConvert() {
    CreateImageRequest createImageRequest =
        OpenAIImageProcessorRequestConverter.convert(openAIImageProcessorConfig, "Test prompt");

    assertThat(createImageRequest.getPrompt()).isEqualTo("Test prompt");
    assertThat(createImageRequest.getN()).isEqualTo(openAIImageProcessorConfig.getN().get());
    assertThat(createImageRequest.getSize()).isEqualTo(openAIImageProcessorConfig.getSize().get());
    assertThat(createImageRequest.getUser()).isEqualTo(openAIImageProcessorConfig.getUser().get());
  }
}
