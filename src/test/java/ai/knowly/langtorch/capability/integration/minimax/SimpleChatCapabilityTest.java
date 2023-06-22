package ai.knowly.langtorch.capability.integration.minimax;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.processor.minimax.chat.MiniMaxChatProcessor;
import ai.knowly.langtorch.processor.minimax.chat.MiniMaxChatProcessorConfig;
import ai.knowly.langtorch.schema.chat.Role;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import ai.knowly.langtorch.util.MiniMaxServiceTestingUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class SimpleChatCapabilityTest {
  @Mock private MiniMaxService miniMaxService;

  @Test
  void simpleTest() {
    // Arrange.
    when(miniMaxService.createChatCompletion(any()))
        .thenReturn(
            MiniMaxServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ChatCompletionRequest.Message.builder()
                    .setSenderType(Role.MINIMAX_BOT.toString())
                    .setText("Changsha is a city in Hunan province, China.")
                    .build()));

    // Act.
    String response =
        new SimpleChatCapability(
                new MiniMaxChatProcessor(
                    miniMaxService, MiniMaxChatProcessorConfig.getDefaultInstance()),
                ConversationMemory.getDefaultInstance())
            .run("Where is Changsha?");

    // Assert.
    assertThat(response).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
