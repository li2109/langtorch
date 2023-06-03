package ai.knowly.langtorch.loader.vertical.markdown;

import ai.knowly.langtorch.loader.LoadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for Markdown files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class MarkdownLoadOption implements LoadOption {
  private String filePath;
}
