package ai.knowly.langtoch.llm.processor.openai.chat;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.io.input.MultiChatMessageInput;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ChatProcessorTest {
  @Mock private OpenAiService openAiService;
  private ChatProcessor chatProcessor;

  @Before
  public void setUp() {
    chatProcessor = new ChatProcessor(openAiService);
  }

  @Test
  public void testRunWithChatProcessorConfig() {
    // Arrange
    List<ChatMessage> messages =
        Arrays.asList(
            ChatMessage.of(Role.USER, "What is the weather today?"),
            ChatMessage.of(Role.ASSISTANT, "The weather today is sunny."));

    com.theokanning.openai.completion.chat.ChatMessage chatMessage =
        new com.theokanning.openai.completion.chat.ChatMessage();
    chatMessage.setRole(Role.ASSISTANT.name().toLowerCase());
    chatMessage.setContent("It's going to be a hot day.");

    ChatCompletionChoice completionChoice = new ChatCompletionChoice();
    completionChoice.setMessage(chatMessage);

    ChatCompletionResult chatCompletionResult = new ChatCompletionResult();
    chatCompletionResult.setChoices(List.of(completionChoice));

    when(openAiService.createChatCompletion(any())).thenReturn(chatCompletionResult);

    // Act
    ChatMessage output = chatProcessor.run(MultiChatMessageInput.of(messages));

    // Assert
    assertThat(output.getRole()).isEqualTo(Role.ASSISTANT);
    assertThat(output.getMessage()).isEqualTo("It's going to be a hot day.");
  }
}
