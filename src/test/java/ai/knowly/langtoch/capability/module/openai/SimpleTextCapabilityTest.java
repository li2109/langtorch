package ai.knowly.langtoch.capability.module.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAiApi;
import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import io.reactivex.Single;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class SimpleTextCapabilityTest {
  @Mock private OpenAiApi openAiApi;

  @Test
  void simpleTest() {
    // Arrange.
    when(openAiApi.createCompletion(any()))
        .thenReturn(
            Single.just(
                OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                    "Changsha is a city in Hunan province, China.")));

    // Act.
    String output =
        SimpleTextCapability.create(OpenAITextProcessor.create(openAiApi))
            .run("Where is Changsha?");

    // Assert.
    assertThat(output).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
