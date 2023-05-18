package ai.knowly.langtoch.llm.processor.openai.chat;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtoch.schema.chat.AssistantMessage;
import ai.knowly.langtoch.schema.chat.ChatMessage;
import ai.knowly.langtoch.schema.chat.Role;
import org.junit.jupiter.api.Test;

final class OpenAIChatProcessorRequestConverterTest {
  @Test
  void testConvertChatMessage() {
    // Arrange.
    ChatMessage chatMessage = AssistantMessage.of("Hello, how can I help you?");

    // Act.
    ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatMessage
        convertedMessage = OpenAIChatProcessorRequestConverter.convertChatMessage(chatMessage);

    // Assert.
    assertThat(convertedMessage.getRole()).isEqualTo(Role.ASSISTANT.toString().toLowerCase());
    assertThat(convertedMessage.getContent()).isEqualTo("Hello, how can I help you?");
  }
}
