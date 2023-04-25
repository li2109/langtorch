package ai.knowly.langtoch.llm.processor.openai.chat;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.io.MultiChatMessage;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.service.OpenAiService;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenAIChatProcessorTest {
  @Mock private OpenAiService openAiService;
  private OpenAIChatProcessor openAIChatProcessor;

  @Before
  public void setUp() {
    openAIChatProcessor = new OpenAIChatProcessor(openAiService);
  }

  @Test
  public void testRunWithChatProcessorConfig() {
    // Arrange
    List<ChatMessage> messages =
        Arrays.asList(
            ChatMessage.of(Role.USER, "What is the weather today?"),
            ChatMessage.of(Role.ASSISTANT, "The weather today is sunny."));

    when(openAiService.createChatCompletion(any()))
        .thenReturn(
            OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ChatMessage.of(Role.ASSISTANT, "It's going to be a hot day.")));

    // Act
    ChatMessage output = openAIChatProcessor.run(MultiChatMessage.of(messages));

    // Assert
    assertThat(output.getRole()).isEqualTo(Role.ASSISTANT);
    assertThat(output.getMessage()).isEqualTo("It's going to be a hot day.");
  }
}
