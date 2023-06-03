package ai.knowly.langtorch.loader.spreadsheet;

import ai.knowly.langtorch.loader.DocumentLoader;
import ai.knowly.langtorch.loader.spreadsheet.SpreadSheetLoadOption.SpreadSheetFormat;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/** Implementation of DocumentConnector for CSV files. */
public class SpreadSheetLoader extends DocumentLoader<SpreadSheetLoadOption> {

  private SpreadSheetLoader() {}

  public static SpreadSheetLoader create() {
    return new SpreadSheetLoader();
  }

  public String read(String filePath) throws IOException {
    return read(SpreadSheetLoadOption.builder().setFilePath(filePath).build());
  }

  @Override
  protected String read(SpreadSheetLoadOption readOption) throws IOException {
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
