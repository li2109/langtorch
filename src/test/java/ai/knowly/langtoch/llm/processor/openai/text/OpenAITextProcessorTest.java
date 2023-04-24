package ai.knowly.langtoch.llm.processor.openai.text;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenAITextProcessorTest {
  @Mock private OpenAiService openAiService;
  private OpenAITextProcessor openAITextProcessor;

  @Before
  public void setUp() {
    openAITextProcessor = new OpenAITextProcessor(openAiService);
  }

  @Test
  public void testRun() {
    // Arrange.
    SingleText inputData = SingleText.of("input1");
    OpenAITextProcessorConfig config =
        OpenAITextProcessorConfig.builder()
            .setModel(OpenAITextProcessor.DEFAULT_MODEL)
            .setMaxTokens(2048)
            .setSuffix("test-suffix")
            .build();

    when(openAiService.createCompletion(
            CompletionRequest.builder()
                .model("text-davinci-003")
                .maxTokens(2048)
                .suffix("test-suffix")
                .prompt("input1")
                .logitBias(Map.of())
                .stop(List.of())
                .build()))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("test-response"));

    // Act.
    SingleText output = openAITextProcessor.withConfig(config).run(inputData);

    // Assert.
    assertThat(output.getText()).isEqualTo("test-response");
  }
}
