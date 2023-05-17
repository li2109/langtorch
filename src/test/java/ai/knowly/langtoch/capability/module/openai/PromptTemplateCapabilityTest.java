package ai.knowly.langtoch.capability.module.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class PromptTemplateCapabilityTest {
  @Mock private OpenAIService openAIService;

  @Test
  void simpleTest() throws ExecutionException, InterruptedException {
    // Arrange.
    when(openAIService.createCompletion(any()))
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    String response =
        PromptTemplateTextCapability.create(OpenAITextProcessor.create(openAIService))
            .withPromptTemplate(
                PromptTemplate.builder().setTemplate("Create name for {{$area}} company").build())
            .run(Map.of("area", "search engine"));

    // Assert.
    assertThat(response).isEqualTo("Google");
  }

  @Test
  public void simpleTest_promptTemplateWithoutVariable()
      throws ExecutionException, InterruptedException {
    // Arrange.
    when(openAIService.createCompletion(any()))
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    String response =
        PromptTemplateTextCapability.create(OpenAITextProcessor.create(openAIService))
            .withPromptTemplate(
                PromptTemplate.builder()
                    .setTemplate("Create name for search engine company")
                    .build())
            .run(Map.of());

    // Assert.
    assertThat(response).isEqualTo("Google");
  }

  @Test
  void promptTemplateWithoutVariable_withInputMap() {
    // Arrange.
    // Act.
    // Assert.
    assertThrows(
        IllegalArgumentException.class,
        () ->
            PromptTemplateTextCapability.create(OpenAITextProcessor.create(openAIService))
                .withPromptTemplate(
                    PromptTemplate.builder()
                        .setTemplate("Create name for search engine company")
                        .build())
                .run(Map.of("area", "search engine")));
  }
}
