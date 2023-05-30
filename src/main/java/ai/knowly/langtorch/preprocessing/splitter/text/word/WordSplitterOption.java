package ai.knowly.langtorch.preprocessing.splitter.text.word;

import ai.knowly.langtorch.preprocessing.splitter.text.SplitterOption;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Options for {@link WordSplitter}. */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class WordSplitterOption extends SplitterOption {
  // Unprocessed text.
  private final String text;

  // The max length of a chunk.
  private final int maxLengthPerChunk;

  private WordSplitterOption(String text, int maxLengthPerChunk) {
    super(text);
    this.text = text;
    this.maxLengthPerChunk = maxLengthPerChunk;
  }

  public static WordSplitterOption of(String text, int totalLengthOfChunk) {
    return new WordSplitterOption(text, totalLengthOfChunk);
  }
}
