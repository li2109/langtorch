package ai.knowly.langtorch.preprocessing.splitter.text.word;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class WordLevelSplitterTest {
  @Test
  void testSplitText_NormalUsage() {
    // Arrange.
    WordLevelSplitterOption option =
        WordLevelSplitterOption.builder()
            .setText("Hello world, this is a test.")
            .setMaxLengthPerChunk(10)
            .build();

    // Act.
    List<String> result = WordLevelSplitter.create().splitText(option);

    // Assert.
    assertThat(result).containsExactly("Hello", "world,", "this is a", "test.").inOrder();
  }

  @Test
  void testSplitText_SingleWord() {
    // Arrange.
    WordLevelSplitterOption option =
        WordLevelSplitterOption.builder().setText("Hello").setMaxLengthPerChunk(10).build();

    // Act.
    List<String> result = WordLevelSplitter.create().splitText(option);

    // Assert.
    assertThat(result).containsExactly("Hello");
  }

  @Test
  void testSplitText_SingleChar() {
    // Arrange.
    WordLevelSplitterOption option =
        WordLevelSplitterOption.builder().setText("H").setMaxLengthPerChunk(1).build();

    // Act.
    List<String> result = WordLevelSplitter.create().splitText(option);

    // Assert.
    assertThat(result).containsExactly("H");
  }

  void testSplitText_MaxLengthSmallerThanWordLength() {
    // Arrange.
    WordLevelSplitterOption option =
        WordLevelSplitterOption.builder().setText("Hello").setMaxLengthPerChunk(3).build();

    // Act.
    // Assert.
    assertThrows(
        IllegalArgumentException.class, () -> WordLevelSplitter.create().splitText(option));
  }

  @Test
  void testSplitText_NegativeMaxLength() {
    // Arrange.
    WordLevelSplitterOption option =
        WordLevelSplitterOption.builder().setText("Hello").setMaxLengthPerChunk(-5).build();

    // Act.
    // Assert.
    assertThrows(
        IllegalArgumentException.class, () -> WordLevelSplitter.create().splitText(option));
  }

  @Test
  void testSplitText_EmptyString() {
    // Arrange.
    WordLevelSplitterOption option =
        WordLevelSplitterOption.builder().setText("").setMaxLengthPerChunk(10).build();

    // Act.
    List<String> result = WordLevelSplitter.create().splitText(option);

    // Assert.
    assertThat(result).isEmpty();
  }
}
