package ai.knowly.langtoch.llm.processor.openai.text;

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
public class TextProcessorTest {
  @Mock private OpenAiService openAiService;
  private TextProcessor textProcessor;

  @Before
  public void setUp() {
    textProcessor = new TextProcessor(openAiService);
  }

  @Test
  public void testRun() {
    // Arrange.
    SingleTextInput inputData = SingleTextInput.of("input1");
    TextProcessorConfig config = TextProcessorConfig.builder().setSuffix("test-suffix").build();

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
    SingleTextOutput output = textProcessor.withConfig(config).run(inputData);

    // Assert.
    assertThat(output.getText()).isEqualTo("test-response");
  }
}
