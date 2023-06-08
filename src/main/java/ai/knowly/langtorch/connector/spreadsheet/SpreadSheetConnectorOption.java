package ai.knowly.langtorch.connector.spreadsheet;

import ai.knowly.langtorch.connector.ConnectorOption;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/** Implementation of ReadOption for CSV files. */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class SpreadSheetConnectorOption implements ConnectorOption {
  @NonNull private String filePath;
  // \n will be used by default if not specified.
  private String separatorForEachLine;
  private SpreadSheetFormat spreadSheetFormat;

  public Optional<String> getSeparatorForEachLine() {
    return Optional.ofNullable(separatorForEachLine);
  }

  public Optional<SpreadSheetFormat> getSpreadSheetFormat() {
    return Optional.ofNullable(spreadSheetFormat);
  }

  public enum SpreadSheetFormat {
    CSV,
    EXCEL,
  }
}
