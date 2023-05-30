package ai.knowly.langtorch.preprocessing.splitter.text.word;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class WordSplitterTest {
  @Test
  void testSplitText_NormalUsage() {
    // Arrange.
    WordSplitterOption option =
        WordSplitterOption.builder()
            .setText("Hello world, this is a test.")
            .setMaxLengthPerChunk(10)
            .build();

    // Act.
    List<String> result = WordSplitter.create().splitText(option);

    // Assert.
    assertThat(result).containsExactly("Hello", "world,", "this is a", "test.").inOrder();
  }

  @Test
  void testSplitText_SingleWord() {
    // Arrange.
    WordSplitterOption option =
        WordSplitterOption.builder().setText("Hello").setMaxLengthPerChunk(10).build();

    // Act.
    List<String> result = WordSplitter.create().splitText(option);

    // Assert.
    assertThat(result).containsExactly("Hello");
  }

  @Test
  void testSplitText_SingleChar() {
    // Arrange.
    WordSplitterOption option =
        WordSplitterOption.builder().setText("H").setMaxLengthPerChunk(1).build();

    // Act.
    List<String> result = WordSplitter.create().splitText(option);

    // Assert.
    assertThat(result).containsExactly("H");
  }

  void testSplitText_MaxLengthSmallerThanWordLength() {
    // Arrange.
    WordSplitterOption option =
        WordSplitterOption.builder().setText("Hello").setMaxLengthPerChunk(3).build();

    // Act.
    // Assert.
    assertThrows(IllegalArgumentException.class, () -> WordSplitter.create().splitText(option));
  }

  @Test
  void testSplitText_NegativeMaxLength() {
    // Arrange.
    WordSplitterOption option =
        WordSplitterOption.builder().setText("Hello").setMaxLengthPerChunk(-5).build();

    // Act.
    // Assert.
    assertThrows(IllegalArgumentException.class, () -> WordSplitter.create().splitText(option));
  }

  @Test
  void testSplitText_EmptyString() {
    // Arrange.
    WordSplitterOption option =
        WordSplitterOption.builder().setText("").setMaxLengthPerChunk(10).build();

    // Act.
    List<String> result = WordSplitter.create().splitText(option);

    // Assert.
    assertThat(result).isEmpty();
  }
}
