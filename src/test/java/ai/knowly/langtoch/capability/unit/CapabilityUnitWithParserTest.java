package ai.knowly.langtoch.capability.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.parser.Parser;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.service.OpenAiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CapabilityUnitWithParserTest {
  @Mock private OpenAiService openAiService;

  @Test
  public void testWithBothParsers() {
    // Arrange.
    when(openAiService.createCompletion(any()))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                "Tokyo is the capital of Japan."));

    StringToSingleTextParser inputParser = new StringToSingleTextParser();
    SingleTextToStringParser outputParser = new SingleTextToStringParser();

    // Act.
    String output =
        CapabilityUnitWithParser.<String, SingleText, SingleText, String>with(
                OpenAITextProcessor.create(openAiService))
            .withInputParser(inputParser)
            .withOutputParser(outputParser)
            .run("What is the capital of Japan?");

    // Assert.
    assertThat(output).isEqualTo("Tokyo is the capital of Japan.");
  }

  class StringToSingleTextParser extends Parser<String, SingleText> {
    @Override
    public SingleText parse(String input) {
      return SingleText.of(input);
    }
  }

  public class SingleTextToStringParser extends Parser<SingleText, String> {
    @Override
    public String parse(SingleText input) {
      return input.getText();
    }
  }
}
