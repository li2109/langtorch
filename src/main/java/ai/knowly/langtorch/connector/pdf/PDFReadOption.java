package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.ReadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for PDF files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PDFReadOption implements ReadOption {
  private String filePath;
}
