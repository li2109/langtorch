package ai.knowly.langtoch.capability.module.openai.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.openai.text.OpenAITextProcessor;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.OpenAiApi;
import io.reactivex.Single;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleTextCapabilityUnitTest {
  @Mock private OpenAiApi openAiApi;

  @Test
  public void simpleTest() throws ExecutionException, InterruptedException {
    // Arrange.
    when(openAiApi.createCompletion(any()))
        .thenReturn(
            Single.just(
                OpenAIServiceTestingUtils.TextCompletion.createCompletionResult(
                    "Changsha is a city in Hunan province, China.")));

    // Act.
    String output =
        SimpleTextCapabilityUnit.create(OpenAITextProcessor.create(openAiApi))
            .run("Where is Changsha?");

    // Assert.
    assertThat(output).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
