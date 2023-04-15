package ai.knowly.langtoch.memory.conversation;

import static com.google.common.truth.Truth.assertThat;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConversationMemoryTest {
  private ConversationMemory conversationMemory;

  @Before
  public void setUp() {
    conversationMemory = new ConversationMemory();
  }

  @Test
  public void testAddAndGet() {
    // Arrange
    conversationMemory.add("key1", "value1");

    // Act
    Optional<String> value = conversationMemory.get("key1");

    // Assert
    assertThat(value.isPresent()).isTrue();
    assertThat(value.get()).isEqualTo("value1");
  }

  @Test
  public void testGetNotPresent() {
    // Act
    Optional<String> value = conversationMemory.get("key1");

    // Assert
    assertThat(value.isEmpty()).isTrue();
  }

  @Test
  public void testRemove() {
    // Arrange
    conversationMemory.add("key1", "value1");
    conversationMemory.remove("key1");

    // Act
    Optional<String> value = conversationMemory.get("key1");

    // Assert
    assertThat(value.isEmpty()).isTrue();
  }

  @Test
  public void testClear() {
    // Arrange
    conversationMemory.add("key1", "value1");
    conversationMemory.add("key2", "value2");
    conversationMemory.clear();

    // Act
    Optional<String> value1 = conversationMemory.get("key1");
    Optional<String> value2 = conversationMemory.get("key2");

    // Assert
    assertThat(value1.isEmpty()).isTrue();
    assertThat(value2.isEmpty()).isTrue();
  }

  @Test
  public void testPreserveInsertionOrder() {
    // Arrange
    conversationMemory.add("key1", "value1");
    conversationMemory.add("key2", "value2");
    conversationMemory.add("key3", "value3");

    String expectedResult = "value1value2value3";
    StringBuilder actualResult = new StringBuilder();

    // Act
    conversationMemory.getMemory().forEach((key, value) -> actualResult.append(value));

    // Assert
    assertThat(actualResult.toString()).isEqualTo(expectedResult);
  }
}
