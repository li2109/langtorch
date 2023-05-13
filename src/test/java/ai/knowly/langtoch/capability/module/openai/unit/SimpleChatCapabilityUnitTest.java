package ai.knowly.langtoch.capability.module.openai.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.chat.Role;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.OpenAiApi;
import io.reactivex.Single;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleChatCapabilityUnitTest {
  @Mock private OpenAiApi openAiApi;

  @Test
  public void simpleTest() throws ExecutionException, InterruptedException {
    // Arrange.
    when(openAiApi.createChatCompletion(any()))
        .thenReturn(
            Single.just(
                OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                    ChatMessage.of(
                        Role.ASSISTANT, "Changsha is a city in Hunan province, China."))));

    // Act.
    String response =
        SimpleChatCapabilityUnit.create(OpenAIChatProcessor.create(openAiApi))
            .run("Where is Changsha?");

    // Assert.
    assertThat(response).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
