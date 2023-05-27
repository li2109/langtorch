package ai.knowly.langtorch.connector.csv;

import ai.knowly.langtorch.connector.ReadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for CSV files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class CSVReadOption implements ReadOption {
  private String filePath;
  private CSVFormat csvFormat;

  enum CSVFormat {
    CSV,
    EXCEL,
  }
}
