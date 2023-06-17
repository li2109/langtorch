package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.ConnectorOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for PDF files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PDFConnectorOption implements ConnectorOption {
  private String filePath;
}
