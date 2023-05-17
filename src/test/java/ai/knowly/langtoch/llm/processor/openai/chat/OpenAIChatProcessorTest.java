package ai.knowly.langtoch.llm.processor.openai.chat;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIApi;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.chat.Role;
import ai.knowly.langtoch.schema.io.MultiChatMessage;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class OpenAIChatProcessorTest {
  @Mock private OpenAIApi OpenAiApi;
  private OpenAIChatProcessor openAIChatProcessor;

  @BeforeEach
  public void setUp() {
    openAIChatProcessor = new OpenAIChatProcessor(OpenAiApi);
  }

  @Test
  void testRunWithChatProcessorConfig() {
    // Arrange
    List<ChatMessage> messages =
        Arrays.asList(
            ChatMessage.of(Role.USER, "What is the weather today?"),
            ChatMessage.of(Role.ASSISTANT, "The weather today is sunny."));

    when(OpenAiApi.createChatCompletion(any()))
        .thenReturn(
            Single.just(
                OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                    ChatMessage.of(Role.ASSISTANT, "It's going to be a hot day."))));

    // Act
    ChatMessage output = openAIChatProcessor.run(MultiChatMessage.of(messages));

    // Assert
    assertThat(output.getRole()).isEqualTo(Role.ASSISTANT);
    assertThat(output.getMessage()).isEqualTo("It's going to be a hot day.");
  }
}
