package ai.knowly.langtoch.capability.module.openai.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.service.OpenAiService;
import java.util.Map;
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
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    String response =
        PromptTemplateTextCapabilityUnit.create(OpenAITextProcessor.create(openAiService))
            .withPromptTemplate(
                PromptTemplate.builder().setTemplate("Create name for {{$area}} company").build())
            .run(Map.of("area", "search engine"));

    // Assert.
    assertThat(response).isEqualTo("Google");
  }

  @Test
  public void simpleTest_promptTemplateWithoutVariable() {
    // Arrange.
    when(openAiService.createCompletion(any()))
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    String response =
        PromptTemplateTextCapabilityUnit.create(OpenAITextProcessor.create(openAiService))
            .withPromptTemplate(
                PromptTemplate.builder()
                    .setTemplate("Create name for search engine company")
                    .build())
            .run(Map.of());

    // Assert.
    assertThat(response).isEqualTo("Google");
  }

  @Test
  public void promptTemplateWithoutVariable_withInputMap() {
    // Arrange.
    when(openAiService.createCompletion(any()))
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    // Assert.
    assertThrows(
        IllegalArgumentException.class,
        () ->
            PromptTemplateTextCapabilityUnit.create(OpenAITextProcessor.create(openAiService))
                .withPromptTemplate(
                    PromptTemplate.builder()
                        .setTemplate("Create name for search engine company")
                        .build())
                .run(Map.of("area", "search engine")));
  }
}
