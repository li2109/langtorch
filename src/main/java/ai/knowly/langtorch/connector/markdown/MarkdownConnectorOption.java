package ai.knowly.langtorch.connector.markdown;

import ai.knowly.langtorch.connector.ConnectorOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for Markdown files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class MarkdownConnectorOption implements ConnectorOption {
  private String filePath;
}
