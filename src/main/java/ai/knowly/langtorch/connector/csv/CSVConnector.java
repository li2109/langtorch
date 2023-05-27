package ai.knowly.langtorch.connector.csv;

import ai.knowly.langtorch.connector.DocumentConnector;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/** Implementation of DocumentConnector for CSV files. */
public class CSVConnector extends DocumentConnector<CSVReadOption> {

  private CSVConnector() {}

  public static CSVConnector create() {
    return new CSVConnector();
  }

  public String read(String filePath) throws IOException {
    return read(CSVReadOption.builder().setFilePath(filePath).build());
  }

  @Override
  protected String read(CSVReadOption readOption) throws IOException {
    StringBuilder sb = new StringBuilder();
    try (FileReader fileReader = new FileReader(readOption.getFilePath())) {
      CSVParser csvParser = new CSVParser(fileReader, convertCSVFormat(readOption.getCsvFormat()));
      for (CSVRecord csvRecord : csvParser) {
        sb.append(csvRecord.toString())
            .append(
                readOption.getSeparatorForEachLine() == null
                    ? "\n"
                    : readOption.getSeparatorForEachLine());
      }
    }

    return sb.toString();
  }

  private CSVFormat convertCSVFormat(CSVReadOption.CSVFormat csvFormat) {
    if (csvFormat == CSVReadOption.CSVFormat.CSV) {
      return CSVFormat.DEFAULT;
    }
    if (csvFormat == CSVReadOption.CSVFormat.EXCEL) {
      return CSVFormat.EXCEL;
    }
    return CSVFormat.DEFAULT;
  }
}
