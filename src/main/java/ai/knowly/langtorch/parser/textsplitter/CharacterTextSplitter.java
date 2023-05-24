package ai.knowly.langtorch.parser.textsplitter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CharacterTextSplitter extends TextSplitter {

    public String separator = "\n\n";

    public CharacterTextSplitter(@Nullable String separator, int chunkSize, int chunkOverlap) {
        super(chunkSize, chunkOverlap);
        if (separator != null) {
            this.separator = separator;
        }
    }

    @Override
    public List<String> splitText(String text) {
        List<String> splits;

        if (this.separator != null) {
            splits = Arrays.asList(text.split(this.separator));
        } else {
            splits = Arrays.asList(text.split(""));
        }

        return mergeSplits(splits, this.separator);
    }
}
