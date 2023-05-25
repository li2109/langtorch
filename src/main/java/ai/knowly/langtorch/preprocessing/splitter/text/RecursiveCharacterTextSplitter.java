package ai.knowly.langtorch.preprocessing.splitter.text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 The RecursiveCharacterTextSplitter class is a concrete implementation of the TextSplitter abstract class
 that recursively splits text into chunks using a set of separators.
 It applies a recursive splitting approach to handle longer texts by examining the text and selecting the appropriate
 separator from the list of separators based on their presence in the text.
 If the text is longer than the chunk size, it recursively splits the longer portions into smaller chunks. This recursive process continues until the chunks reach a size smaller than the specified chunk size.
 */
public class RecursiveCharacterTextSplitter extends TextSplitter {

    private static List<String> separators = Arrays.asList("\n\n", "\n", " ", "");

    /**
     Constructs a RecursiveCharacterTextSplitter object with the given list of separators, chunk size, and chunk overlap.
     If the separators list is null, the default list containing separators "\n\n", "\n", " ", and "" is used.
     @param separators The list of separators used for splitting the text into chunks.
     @param chunkSize The size of each chunk.
     @param chunkOverlap The amount of overlap between adjacent chunks.
     */
    public RecursiveCharacterTextSplitter(@Nullable List<String> separators, int chunkSize, int chunkOverlap) {
        super(chunkSize, chunkOverlap);
        if (separators != null) {
            this.separators = separators;
        }
    }

    /**
     Splits the given text into chunks using a recursive splitting approach.
     It selects an appropriate separator from the list of separators based on the presence of each separator in the text.
     It recursively splits longer pieces of text into smaller chunks.
     @param text The text to be split into chunks.
     @return A list of strings representing the chunks of the text.
     */
    @Override
    public List<String> splitText(String text) {
        List<String> finalChunks = new ArrayList<>();

        // Get appropriate separator to use
        String separator = separators.get(separators.size() - 1);
        for (String s : separators) {
            if (s.isEmpty() || text.contains(s)) {
                separator = s;
                break;
            }
        }

        // Now that we have the separator, split the text
        String[] splits;
        if (!separator.isEmpty()) {
            splits = text.split(separator);
        } else {
            splits = text.split("");
        }

        // Now go merging things, recursively splitting longer texts
        List<String> goodSplits = new ArrayList<>();
        for (String s : splits) {
            if (s.length() < chunkSize) {
                goodSplits.add(s);
            } else {
                if (!goodSplits.isEmpty()) {
                    List<String> mergedText = mergeSplits(goodSplits, separator);
                    finalChunks.addAll(mergedText);
                    goodSplits.clear();
                }
                List<String> otherInfo = splitText(s);
                finalChunks.addAll(otherInfo);

            }
        }
        if (!goodSplits.isEmpty()) {
            List<String> mergedText = mergeSplits(goodSplits, separator);
            finalChunks.addAll(mergedText);
        }
        return finalChunks;

    }
}
