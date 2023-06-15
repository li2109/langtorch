package ai.knowly.langtorch.capability.integration.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtorch.processor.openai.text.OpenAITextProcessorConfig;
import ai.knowly.langtorch.prompt.template.PromptTemplate;
import ai.knowly.langtorch.util.OpenAIServiceTestingUtils;
import com.google.common.collect.ImmutableMap;
import com.google.inject.testing.fieldbinder.Bind;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class PromptTemplateCapabilityTest {
  @Mock @Bind private OpenAIService openAIService;

  @Test
  void simpleTest() {
    // Arrange.
    when(openAIService.createCompletion(any()))
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    String response =
        new PromptTemplateTextCapability(
                new OpenAITextProcessor(
                    openAIService, OpenAITextProcessorConfig.getDefaultInstance()),
                PromptTemplate.builder().setTemplate("Create name for {{$area}} company").build())
            .run(ImmutableMap.of("area", "search engine"));

    // Assert.
    assertThat(response).isEqualTo("Google");
  }

  @Test
  void simpleTest_promptTemplateWithoutVariable() throws ExecutionException, InterruptedException {
    // Arrange.
    when(openAIService.createCompletion(any()))
        .thenReturn(OpenAIServiceTestingUtils.TextCompletion.createCompletionResult("Google"));

    // Act.
    String response =
        new PromptTemplateTextCapability(
                new OpenAITextProcessor(
                    openAIService, OpenAITextProcessorConfig.getDefaultInstance()),
                PromptTemplate.builder()
                    .setTemplate("Create name for search engine company")
                    .build())
            .run(new HashMap<>());

    // Assert.
    assertThat(response).isEqualTo("Google");
  }

  @Test
  void promptTemplateWithoutVariable_withInputMap() {
    // Arrange.
    PromptTemplate promptTemplate =
        PromptTemplate.builder().setTemplate("Create name for search engine company").build();
    PromptTemplateTextCapability promptTemplateTextCapability =
        new PromptTemplateTextCapability(
            new OpenAITextProcessor(openAIService, OpenAITextProcessorConfig.getDefaultInstance()),
            promptTemplate);
    ImmutableMap<String, String> variables = ImmutableMap.of("area", "search engine");
    // Act.
    // Assert.
    assertThrows(IllegalArgumentException.class, () -> promptTemplateTextCapability.run(variables));
  }
}
