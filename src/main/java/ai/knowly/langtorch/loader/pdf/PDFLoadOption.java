package ai.knowly.langtorch.loader.pdf;

import ai.knowly.langtorch.loader.LoadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for PDF files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PDFLoadOption implements LoadOption {
  private String filePath;
}
