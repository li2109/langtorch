package ai.knowly.langtorch.preprocessing.splitter.text.word;

import ai.knowly.langtorch.preprocessing.splitter.text.SplitterOption;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Options for {@link WordLevelSplitter}. */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class WordLevelSplitterOption extends SplitterOption {
  // Unprocessed text.
  private final String text;

  // The max length of a chunk.
  private final int maxLengthPerChunk;

  private WordLevelSplitterOption(String text, int maxLengthPerChunk) {
    super(text);
    this.text = text;
    this.maxLengthPerChunk = maxLengthPerChunk;
  }

  public static WordLevelSplitterOption of(String text, int totalLengthOfChunk) {
    return new WordLevelSplitterOption(text, totalLengthOfChunk);
  }
}
