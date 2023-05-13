package ai.knowly.langtoch.llm.processor.openai.image;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.schema.image.Images;
import ai.knowly.langtoch.schema.io.SingleText;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.image.ImageResult;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenAIImageProcessorTest {
  @Mock private OpenAiApi openAiApi;
  private OpenAIImageProcessor openAIImageProcessor;

  @Before
  public void setUp() {
    openAIImageProcessor = new OpenAIImageProcessor(openAiApi);
  }

  @Test
  public void testRun() {
    // Arrange.
    SingleText inputData = SingleText.of("image description");
    OpenAIImageProcessorConfig config = OpenAIImageProcessorConfig.builder().setN(2).build();

    ImageResult expectedResult = new ImageResult();
    expectedResult.setCreated(123L);
    Image image1 = new Image();
    image1.setUrl("image1-url");
    Image image2 = new Image();
    image1.setUrl("image2-url");

    expectedResult.setData(Arrays.asList(image1, image2));

    when(openAiApi.createImage(
            OpenAIImageProcessorRequestConverter.convert(config, "image description")))
        .thenReturn(Single.just(expectedResult));

    // Act.
    Images output = openAIImageProcessor.withConfig(config).run(inputData);

    // Assert.
    assertThat(output.getCreated()).isEqualTo(expectedResult.getCreated());
    assertThat(
            output.getImages().stream().map(image -> image.getUrl()).collect(Collectors.toList()))
        .containsExactlyElementsIn(
            expectedResult.getData().stream()
                .map(data -> data.getUrl())
                .collect(Collectors.toList()));
  }
}
