package ai.knowly.langtorch.connector.spreadsheet;

import ai.knowly.langtorch.connector.DocumentConnector;
import ai.knowly.langtorch.connector.spreadsheet.SpreadSheetConnectorOption.SpreadSheetFormat;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/** Implementation of DocumentConnector for CSV files. */
public class SpreadSheetConnector extends DocumentConnector<SpreadSheetConnectorOption> {

  private SpreadSheetConnector() {}

  public static SpreadSheetConnector create() {
    return new SpreadSheetConnector();
  }

  public String read(String filePath) throws IOException {
    return read(SpreadSheetConnectorOption.builder().setFilePath(filePath).build());
  }

  @Override
  protected String read(SpreadSheetConnectorOption readOption) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (FileReader fileReader = new FileReader(readOption.getFilePath())) {
      CSVParser csvParser =
          new CSVParser(fileReader, convertCSVFormat(readOption.getSpreadSheetFormat()));
      for (CSVRecord csvRecord : csvParser) {
        sb.append(csvRecord.toString()).append(readOption.getSeparatorForEachLine().orElse("\n"));
      }
    }

    return sb.toString();
  }

  private CSVFormat convertCSVFormat(Optional<SpreadSheetFormat> spreadSheetFormat) {
    if (!spreadSheetFormat.isPresent()) {
      return CSVFormat.DEFAULT;
    }
    if (SpreadSheetFormat.CSV == spreadSheetFormat.get()) {
      return CSVFormat.DEFAULT;
    }
    if (SpreadSheetFormat.EXCEL == spreadSheetFormat.get()) {
      return CSVFormat.EXCEL;
    }
    return CSVFormat.DEFAULT;
  }
}
