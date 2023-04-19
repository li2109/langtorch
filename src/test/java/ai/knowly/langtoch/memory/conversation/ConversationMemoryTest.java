package ai.knowly.langtoch.memory.conversation;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtoch.llm.message.AssistantMessage;
import ai.knowly.langtoch.llm.message.BaseChatMessage;
import ai.knowly.langtoch.llm.message.Role;
import ai.knowly.langtoch.llm.message.UserMessage;
import com.google.common.collect.ImmutableList;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConversationMemoryTest {
  private ConversationMemory conversationMemory;

  @Before
  public void setUp() {
    conversationMemory = ConversationMemory.create();
  }

  @Test
  public void testAddAndGet() {
    // Arrange
    conversationMemory.add(
        Role.USER, UserMessage.builder().setMessage("Hi, how's whether like today?").build());

    // Act
    Iterable<BaseChatMessage> messages = conversationMemory.get(Role.USER);

    // Assert
    assertThat(messages)
        .containsExactly(UserMessage.builder().setMessage("Hi, how's whether like today?").build());
  }

  @Test
  public void testGetNotPresent() {
    // Act
    Iterable<BaseChatMessage> messages = conversationMemory.get(Role.USER);

    // Assert
    assertThat(messages).isEmpty();
  }

  @Test
  public void testClear() {
    // Arrange
    conversationMemory.add(
        Role.USER, UserMessage.builder().setMessage("Hi, how's whether like today?").build());
    conversationMemory.add(
        Role.ASSISTANT,
        UserMessage.builder().setMessage("It's sunny in Mountain View, CA.").build());
    conversationMemory.clear();

    // Act
    Iterable<BaseChatMessage> userMessages = conversationMemory.get(Role.USER);
    Iterable<BaseChatMessage> assistantMessages = conversationMemory.get(Role.ASSISTANT);

    // Assert
    assertThat(userMessages).isEmpty();
    assertThat(assistantMessages).isEmpty();
  }

  @Test
  public void testPreserveInsertionOrder() {
    // Arrange
    conversationMemory.add(
        Role.ASSISTANT,
        AssistantMessage.builder().setMessage("Hi, how can i help you today?").build());
    conversationMemory.add(
        Role.USER, UserMessage.builder().setMessage("how's whether like today?").build());
    conversationMemory.add(
        Role.ASSISTANT,
        AssistantMessage.builder().setMessage("It's sunny in Mountain View, CA.").build());

    // Act
    ImmutableList<BaseChatMessage> actual =
        conversationMemory.getMemory().entries().stream()
            .map(Entry::getValue)
            .collect(toImmutableList());

    // Assert
    assertThat(actual.get(0).getMessage()).isEqualTo("Hi, how can i help you today?");
    assertThat(actual.get(0).getRole()).isEqualTo(Role.ASSISTANT);

    assertThat(actual.get(1).getMessage()).isEqualTo("how's whether like today?");
    assertThat(actual.get(1).getRole()).isEqualTo(Role.USER);

    assertThat(actual.get(2).getMessage()).isEqualTo("It's sunny in Mountain View, CA.");
    assertThat(actual.get(2).getRole()).isEqualTo(Role.ASSISTANT);
  }
}
