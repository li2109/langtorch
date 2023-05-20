package ai.knowly.langtoch.llm.processor.openai.text;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.dto.completion.CompletionRequest;
import ai.knowly.langtoch.schema.io.SingleText;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class OpenAITextProcessorTest {
  @Mock private OpenAIService openAIService;
  private OpenAITextProcessor openAITextProcessor;

  @BeforeEach
  void setUp() {
    openAITextProcessor = new OpenAITextProcessor(openAIService);
  }

  @Test
  void testRun() throws ExecutionException, InterruptedException {
    // Arrange.
    SingleText inputData = SingleText.of("input1");
    OpenAITextProcessorConfig config =
        OpenAITextProcessorConfig.builder()
            .setModel(OpenAITextProcessor.DEFAULT_MODEL)
            .setMaxTokens(2048)
            .setSuffix("test-suffix")
            .build();

    when(openAIService.createCompletion(
            CompletionRequest.builder()
                .model("text-davinci-003")
                .maxTokens(2048)
                .suffix("test-suffix")
                .prompt("input1")
                .logitBias(Map.of())
                .build()))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("test-response"));

    // Act.
    SingleText output = openAITextProcessor.withConfig(config).run(inputData);

    // Assert.
    assertThat(output.getText()).isEqualTo("test-response");
  }
}
