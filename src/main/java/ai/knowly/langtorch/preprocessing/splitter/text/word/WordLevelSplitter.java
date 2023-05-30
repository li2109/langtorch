package ai.knowly.langtorch.preprocessing.splitter.text.word;

import ai.knowly.langtorch.preprocessing.splitter.text.BaseTextSplitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;

/** Splits text into chunks of words. */
public class WordLevelSplitter implements BaseTextSplitter<WordLevelSplitterOption> {

  public static WordLevelSplitter create() {
    return new WordLevelSplitter();
  }

  @Override
  public List<String> splitText(WordLevelSplitterOption option) {
    int maxLengthPerChunk = option.getMaxLengthPerChunk();
    String text = option.getText();

    Builder<String> chunks = ImmutableList.builder();

    // Validate the maxLengthPerChunk
    if (maxLengthPerChunk < 1) {
      throw new IllegalArgumentException("maxLengthPerChunk should be greater than 0");
    }

    String[] words = text.split("\\s+");
    int minLengthOfWord = words[0].length();

    for (String word : words) {
      minLengthOfWord = Math.min(minLengthOfWord, word.length());
    }

    if (maxLengthPerChunk < minLengthOfWord) {
      throw new IllegalArgumentException(
          "maxLengthPerChunk is smaller than the smallest word in the string");
    }

    StringBuilder chunk = new StringBuilder();
    int wordsLength = words.length;

    for (int i = 0; i < wordsLength; i++) {
      String word = words[i];
      boolean isLastWord = i == wordsLength - 1;
      if ((chunk.length() + word.length() + (isLastWord ? 0 : 1))
          <= maxLengthPerChunk) { // '+1' accounts for spaces, except for the last word
        chunk.append(word);
        if (!isLastWord) {
          chunk.append(" ");
        }
      } else {
        chunks.add(chunk.toString().trim());
        chunk = new StringBuilder();
        chunk.append(word).append(" ");
      }
    }

    // Add remaining chunk if any
    if (chunk.length() > 0) {
      chunks.add(chunk.toString().trim());
    }

    return chunks.build();
  }
}
