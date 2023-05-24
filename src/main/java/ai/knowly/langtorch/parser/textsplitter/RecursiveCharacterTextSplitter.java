package ai.knowly.langtorch.parser.textsplitter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecursiveCharacterTextSplitter extends TextSplitter {

    private List<String> separators = Arrays.asList("\n\n", "\n", " ", "");

    public RecursiveCharacterTextSplitter(@Nullable List<String> separators, int chunkSize, int chunkOverlap) {
        super(chunkSize, chunkOverlap);
        if (separators != null) {
            this.separators = separators;
        }
    }

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
