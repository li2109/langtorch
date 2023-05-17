package ai.knowly.langtoch.capability.module.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.chat.Role;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class SimpleChatCapabilityTest {
  @Mock private OpenAIService openAIService;

  @Test
  void simpleTest() {
    // Arrange.
    when(openAIService.createChatCompletion(any()))
        .thenReturn(
            OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ChatMessage.of(Role.ASSISTANT, "Changsha is a city in Hunan province, China.")));

    // Act.
    String response =
        SimpleChatCapability.create(OpenAIChatProcessor.create(openAIService))
            .run("Where is Changsha?");

    // Assert.
    assertThat(response).isEqualTo("Changsha is a city in Hunan province, China.");
  }
}
