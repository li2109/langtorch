package ai.knowly.langtorch.connector.factory;

import ai.knowly.langtorch.connector.ConnectorOption;
import ai.knowly.langtorch.connector.Connector;
import ai.knowly.langtorch.connector.pdf.PDFConnector;
import ai.knowly.langtorch.connector.spreadsheet.SpreadSheetConnector;
import ai.knowly.langtorch.connector.sql.MySQLConnector;
import lombok.NoArgsConstructor;

@SuppressWarnings("unchecked")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LoaderFactory {
  public static <O, R extends ConnectorOption> Connector<O, R> createLoader(LoadType loadType) {
    switch (loadType) {
      case PDF:
        return (Connector<O, R>) PDFConnector.create();
      case SPREADSHEET:
        return (Connector<O, R>) SpreadSheetConnector.create();
      case SQL:
        return (Connector<O, R>) MySQLConnector.create();
      default:
        throw new IllegalArgumentException("Invalid load type: " + loadType);
    }
  }
}
