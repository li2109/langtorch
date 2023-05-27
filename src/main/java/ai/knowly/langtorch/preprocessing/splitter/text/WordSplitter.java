package ai.knowly.langtorch.preprocessing.splitter.text;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
/**
 The CharacterTextSplitter class is a concrete implementation of the TextSplitter abstract class
 that splits text into chunks based on a specified separator.
 */

public class WordSplitter extends TextSplitter {

    private static String separator = "\n\n";

    /**

     Constructs a CharacterTextSplitter object with the given separator, chunk size, and chunk overlap.
     If the separator is null, the default separator "\n\n" is used.
     @param separator The separator used for splitting the text into chunks.
     @param chunkSize The size of each chunk.
     @param chunkOverlap The amount of overlap between adjacent chunks.
     */
    public WordSplitter(@Nullable String separator, int chunkSize, int chunkOverlap) {
        super(chunkSize, chunkOverlap);
        if (separator != null) {
            this.separator = separator;
        }
    }

    /**
     Splits the given text into chunks based on the specified separator.
     @param text The text to be split into chunks.
     @return A list of strings representing the chunks of the text.
     */
    @Override
    public List<String> splitText(String text) {
        List<String> splits =
                Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(text, this.separator.isEmpty() ? "" : this.separator));
        return mergeSplits(splits, this.separator);
    }
}
