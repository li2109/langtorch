package ai.knowly.langtorch.connector.csv;

import ai.knowly.langtorch.connector.ReadOption;
import lombok.Builder;
import lombok.Data;

/** Implementation of ReadOption for CSV files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class CSVReadOption implements ReadOption {
  private String filePath;
  // \n will be used by default if not specified.
  private String separatorForEachLine;
  private CSVFormat csvFormat;

  @Builder
  public CSVReadOption(String filePath, String separatorForEachLine, CSVFormat csvFormat) {
    if (filePath == null || filePath.trim().isEmpty()) {
      throw new IllegalArgumentException("filePath must be set");
    }
    this.filePath = filePath;
    this.separatorForEachLine = separatorForEachLine;
    this.csvFormat = csvFormat;
  }

  enum CSVFormat {
    CSV,
    EXCEL,
  }
}
