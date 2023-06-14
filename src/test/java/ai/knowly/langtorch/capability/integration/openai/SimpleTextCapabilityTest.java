package ai.knowly.langtorch.capability.integration.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.processor.module.openai.text.OpenAITextProcessor;
import ai.knowly.langtorch.util.OpenAIServiceTestingUtils;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class SimpleTextCapabilityTest {
  @Mock @Bind private OpenAIService openAIService;

  @Test
  void simpleTest() {
    // Arrange.
    when(openAIService.createCompletion(any()))
        .thenReturn(
            OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                "Changsha is a city in Hunan province, China."));

    // Act.
    String output =
        new SimpleTextCapability(OpenAITextProcessor.create(openAIService))
            .run("Where is Changsha?");

    // Assert.
    assertThat(output).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
