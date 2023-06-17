package ai.knowly.langtorch.connector.spreadsheet;

/** Exception thrown when a Spreadsheet file cannot be read. */
public class SpreadsheetReadException extends RuntimeException {
  public SpreadsheetReadException(Exception e) {
    super(e);
  }
}
