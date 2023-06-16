package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.ConnectorOption;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

/** Implementation of ReadOption for PDF files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PDFConnectorOption implements ConnectorOption {
  @Builder.Default private Optional<String> filePath = Optional.empty();
  @Builder.Default private Optional<byte[]> bytes = Optional.empty();
}
