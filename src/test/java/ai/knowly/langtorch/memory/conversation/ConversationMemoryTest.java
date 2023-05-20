package ai.knowly.langtorch.memory.conversation;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.schema.chat.AssistantMessage;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.Role;
import ai.knowly.langtorch.schema.chat.UserMessage;
import com.google.common.collect.ImmutableList;
import java.util.Map.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class ConversationMemoryTest {
  private ConversationMemory conversationMemory;

  @BeforeEach
  void setUp() {
    conversationMemory = ConversationMemory.create();
  }

  @Test
  void testAddAndGet() {
    // Arrange
    conversationMemory.add(Role.USER, UserMessage.of("Hi, how's whether like today?"));

    // Act
    Iterable<ChatMessage> messages = conversationMemory.get(Role.USER);

    // Assert
    assertThat(messages).containsExactly(UserMessage.of("Hi, how's whether like today?"));
  }

  @Test
  void testGetNotPresent() {
    // Act
    Iterable<ChatMessage> messages = conversationMemory.get(Role.USER);

    // Assert
    assertThat(messages).isEmpty();
  }

  @Test
  void testClear() {
    // Arrange
    conversationMemory.add(Role.USER, UserMessage.of("Hi, how's whether like today?"));
    conversationMemory.add(Role.ASSISTANT, AssistantMessage.of("It's sunny in Mountain View, CA."));
    conversationMemory.clear();

    // Act
    Iterable<ChatMessage> userMessages = conversationMemory.get(Role.USER);
    Iterable<ChatMessage> assistantMessages = conversationMemory.get(Role.ASSISTANT);

    // Assert
    assertThat(userMessages).isEmpty();
    assertThat(assistantMessages).isEmpty();
  }

  @Test
  void testPreserveInsertionOrder() {
    // Arrange
    conversationMemory.add(Role.ASSISTANT, AssistantMessage.of("Hi, how can i help you today?"));
    conversationMemory.add(Role.USER, UserMessage.of("how's whether like today?"));
    conversationMemory.add(Role.ASSISTANT, AssistantMessage.of("It's sunny in Mountain View, CA."));

    // Act
    ImmutableList<ChatMessage> actual =
        conversationMemory.getMemory().entries().stream()
            .map(Entry::getValue)
            .collect(toImmutableList());

    // Assert
    assertThat(actual.get(0).getContent()).isEqualTo("Hi, how can i help you today?");
    assertThat(actual.get(0).getRole()).isEqualTo(Role.ASSISTANT);

    assertThat(actual.get(1).getContent()).isEqualTo("how's whether like today?");
    assertThat(actual.get(1).getRole()).isEqualTo(Role.USER);

    assertThat(actual.get(2).getContent()).isEqualTo("It's sunny in Mountain View, CA.");
    assertThat(actual.get(2).getRole()).isEqualTo(Role.ASSISTANT);
  }
}
