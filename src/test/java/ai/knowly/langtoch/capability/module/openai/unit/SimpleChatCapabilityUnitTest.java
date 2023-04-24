package ai.knowly.langtoch.capability.module.openai.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtoch.capability.module.openai.unit.SimpleChatCapabilityUnit;
import ai.knowly.langtoch.llm.processor.openai.chat.OpenAIChatProcessor;
import ai.knowly.langtoch.llm.schema.chat.ChatMessage;
import ai.knowly.langtoch.llm.schema.chat.Role;
import ai.knowly.langtoch.llm.schema.io.MultiChatMessage;
import ai.knowly.langtoch.util.OpenAIServiceTestingUtils;
import com.theokanning.openai.service.OpenAiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleChatCapabilityUnitTest {
  @Mock private OpenAiService openAiService;

  @Test
  public void simpleTest() {
    // Arrange.
    when(openAiService.createChatCompletion(any()))
        .thenReturn(
            OpenAIServiceTestingUtils.ChatCompletion.createChatCompletionResult(
                ChatMessage.of(Role.ASSISTANT, "Changsha is a city in Hunan province, China.")));

    // Act.
    ChatMessage response =
        SimpleChatCapabilityUnit.with(OpenAIChatProcessor.create(openAiService))
            .run(MultiChatMessage.of(ChatMessage.of(Role.USER, "Where is Changsha?")));

    // Assert.
    assertThat(response.getMessage()).isEqualTo("Changsha is a city in Hunan province, China.");
    assertThat(response.getRole()).isEqualTo(Role.ASSISTANT);
  }
}
