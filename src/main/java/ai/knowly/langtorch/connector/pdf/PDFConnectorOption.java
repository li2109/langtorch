package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.ConnectorOption;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

/** Implementation of ReadOption for PDF files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PDFConnectorOption implements ConnectorOption {
  private String filePath;
  private byte[] fileBytes;

  public Optional<String> getFilePath() {
    return Optional.ofNullable(filePath);
  }

  public Optional<byte[]> getFileBytes() {
    return Optional.ofNullable(fileBytes);
  }
}
