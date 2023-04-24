package ai.knowly.langtoch.capability.module.openai.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.capability.unit.CapabilityUnitWithParser;
import ai.knowly.langtoch.llm.processor.Processor;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.service.OpenAiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PromptTemplateCapabilityUnitTest {
  @Mock private OpenAiService openAiService;

  @Test
  public void simpleTest() {
    // Arrange.
    when(openAiService.createCompletion(any()))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                "Changsha is a city in Hunan province, China."));

    // Act.
    PromptTemplateTextCapabilityUnit with = PromptTemplateTextCapabilityUnit.with(
        OpenAITextProcessor.create(openAiService));

    with.run(SingleText.of("Changsha is a city in Hunan province, China."));

    // Assert.
    assertThat(output.getText()).isEqualTo("Changsha is a city in Hunan province, China.");
  }

  @Test
  public void testWithFunction_reverseTextProcessor() {
    // Arrange.
    ReverseTextProcessor reverseTextProcessor = ReverseTextProcessor.create();

    // Act.
    SingleText output =
        SimpleTextCapabilityUnit.with(reverseTextProcessor).run(SingleText.of("Hello, World!"));

    // Assert.
    assertThat(output.getText()).isEqualTo("!dlroW ,olleH");
  }

  static class ReverseTextProcessor implements Processor<SingleText, SingleText> {
    public static ReverseTextProcessor create() {
      return new ReverseTextProcessor();
    }

    @Override
    public SingleText run(SingleText inputData) {
      StringBuilder reversed = new StringBuilder(inputData.getText()).reverse();
      return SingleText.of(reversed.toString());
    }
  }
}
