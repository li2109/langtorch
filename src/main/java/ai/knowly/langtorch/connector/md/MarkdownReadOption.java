package ai.knowly.langtorch.connector.md;

import ai.knowly.langtorch.connector.ReadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for Markdown files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class MarkdownReadOption implements ReadOption {
    private String filePath;
}
