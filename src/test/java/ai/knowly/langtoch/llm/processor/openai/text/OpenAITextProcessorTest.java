package ai.knowly.langtoch.llm.processor.openai.text;

import static ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor.DEFAULT_MODEL;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.schema.io.input.SingleTextInput;
import ai.knowly.langtoch.llm.schema.io.output.SingleTextOutput;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
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
    SingleTextInput inputData = SingleTextInput.of("input1");
    OpenAITextProcessorConfig config =
        OpenAITextProcessorConfig.builder()
            .setModel(DEFAULT_MODEL)
            .setMaxTokens(2048)
            .setSuffix("test-suffix")
            .build();

    CompletionResult completionResult = new CompletionResult();
    CompletionChoice completionChoice = new CompletionChoice();
    completionChoice.setText("test-response");
    completionResult.setChoices(List.of(completionChoice));

    when(openAiService.createCompletion(
            CompletionRequest.builder()
                .model("text-davinci-003")
                .maxTokens(2048)
                .suffix("test-suffix")
                .prompt("input1")
                .logitBias(Map.of())
                .stop(List.of())
                .build()))
        .thenReturn(completionResult);

    // Act.
    SingleTextOutput output = openAITextProcessor.withConfig(config).run(inputData);

    // Assert.
    assertThat(output.getText()).isEqualTo("test-response");
  }
}
