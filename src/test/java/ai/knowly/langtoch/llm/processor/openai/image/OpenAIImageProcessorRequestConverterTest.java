package ai.knowly.langtoch.llm.processor.openai.image;

import static com.google.common.truth.Truth.assertThat;

import com.theokanning.openai.image.CreateImageRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class OpenAIImageProcessorRequestConverterTest {

  private OpenAIImageProcessorConfig openAIImageProcessorConfig;

  @Before
  public void setUp() {
    openAIImageProcessorConfig =
        OpenAIImageProcessorConfig.builder().setN(5).setSize("512x512").setUser("user123").build();
  }

  @Test
  public void testConvert() {
    CreateImageRequest createImageRequest =
        OpenAIImageProcessorRequestConverter.convert(openAIImageProcessorConfig, "Test prompt");

    assertThat(createImageRequest.getPrompt()).isEqualTo("Test prompt");
    assertThat(createImageRequest.getN()).isEqualTo(openAIImageProcessorConfig.getN().get());
    assertThat(createImageRequest.getSize()).isEqualTo(openAIImageProcessorConfig.getSize().get());
    assertThat(createImageRequest.getUser()).isEqualTo(openAIImageProcessorConfig.getUser().get());
  }
}
