package ai.knowly.langtorch.processor.module.minimax.chat;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.schema.chat.*;
import ai.knowly.langtorch.schema.text.MultiChatMessage;
import ai.knowly.langtorch.util.MiniMaxServiceTestingUtils;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author maxiao
 * @date 2023/06/10
 */
@ExtendWith(MockitoExtension.class)
final class MiniMaxChatProcessorTest {
  @Mock private MiniMaxService miniMaxService;
  private MiniMaxChatProcessor miniMaxChatProcessor;

  @BeforeEach
  public void setUp() {
    miniMaxChatProcessor = new MiniMaxChatProcessor(miniMaxService);
  }

  @Test
  void testRunWithChatProcessorConfig() {
    // Arrange
    List<ChatMessage> messages =
        Arrays.asList(
            MiniMaxUserMessage.of("What is the weather today?"),
            MiniMaxBotMessage.of("The weather today is sunny."));

    when(miniMaxService.createChatCompletion(any()))
        .thenReturn(
            MiniMaxServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ChatCompletionRequest.Message.builder()
                    .setSenderType("BOT")
                    .setText("It's going to be a hot day.")
                    .build()));

    // Act
    ChatMessage output = miniMaxChatProcessor.run(MultiChatMessage.of(messages));

    // Assert
    assertThat(output.getContent()).isEqualTo("It's going to be a hot day.");
  }
}
