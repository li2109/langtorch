package ai.knowly.langtoch.capability.module.openai.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.llm.schema.io.SingleText;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
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
    SingleText response =
        PromptTemplateTextCapabilityUnit.create(OpenAITextProcessor.create(openAiService))
            .run(
                PromptTemplate.builder()
                    .setTemplate("Changsha is a city in Hunan province, China.")
                    .build());

    // Assert.
    assertThat(response.getText()).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
