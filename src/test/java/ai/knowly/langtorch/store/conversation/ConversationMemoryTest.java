package ai.knowly.langtorch.store.conversation;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.schema.chat.AssistantMessage;
import ai.knowly.langtorch.schema.chat.ChatMessage;
import ai.knowly.langtorch.schema.chat.Role;
import ai.knowly.langtorch.schema.chat.UserMessage;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class ConversationMemoryTest {
  private ConversationMemory conversationMemory;

  @BeforeEach
  void setUp() {
    conversationMemory = ConversationMemory.builder().build();
  }

  @Test
  void testAddAndGet() {
    // Arrange.
    conversationMemory.add(UserMessage.of("Hi, how's whether like today?"));

    // Act.
    Iterable<ChatMessage> messages = conversationMemory.getAll();

    // Assert.
    assertThat(messages).containsExactly(UserMessage.of("Hi, how's whether like today?"));
  }

  @Test
  void testGetNotPresent() {
    // Act.
    Iterable<ChatMessage> messages = conversationMemory.getAll();

    // Assert.
    assertThat(messages).isEmpty();
  }

  @Test
  void emptyMemoryContext() {
    // Act.
    String context = conversationMemory.getMemoryContext().get();

    // Assert.
    assertThat(context).isEmpty();
  }

  @Test
  void testClear() {
    // Arrange.
    conversationMemory.add(UserMessage.of("Hi, how's whether like today?"));
    conversationMemory.add(AssistantMessage.of("It's sunny in Mountain View, CA."));
    conversationMemory.clear();

    // Act.
    Iterable<ChatMessage> userMessages =
        conversationMemory.getAll().stream()
            .filter(chatMessage -> chatMessage.getRole() == Role.USER)
            .collect(toImmutableList());

    Iterable<ChatMessage> assistantMessages =
        conversationMemory.getAll().stream()
            .filter(chatMessage -> chatMessage.getRole() == Role.ASSISTANT)
            .collect(toImmutableList());

    // Assert.
    assertThat(userMessages).isEmpty();
    assertThat(assistantMessages).isEmpty();
  }

  @Test
  void testPreserveInsertionOrder() {
    // Arrange.
    conversationMemory.add(AssistantMessage.of("Hi, how can i help you today?"));
    conversationMemory.add(UserMessage.of("how's whether like today?"));
    conversationMemory.add(AssistantMessage.of("It's sunny in Mountain View, CA."));

    // Act.
    List<ChatMessage> actual = conversationMemory.getAll();

    // Assert.
    assertThat(actual.get(0).getContent()).isEqualTo("Hi, how can i help you today?");
    assertThat(actual.get(0).getRole()).isEqualTo(Role.ASSISTANT);

    assertThat(actual.get(1).getContent()).isEqualTo("how's whether like today?");
    assertThat(actual.get(1).getRole()).isEqualTo(Role.USER);

    assertThat(actual.get(2).getContent()).isEqualTo("It's sunny in Mountain View, CA.");
    assertThat(actual.get(2).getRole()).isEqualTo(Role.ASSISTANT);
  }
}
