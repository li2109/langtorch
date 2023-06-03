package ai.knowly.langtorch.loader.factory;

import ai.knowly.langtorch.loader.LoadOption;
import ai.knowly.langtorch.loader.Loader;
import ai.knowly.langtorch.loader.vertical.pdf.PDFLoader;
import ai.knowly.langtorch.loader.vertical.spreadsheet.SpreadSheetLoader;
import ai.knowly.langtorch.loader.vertical.sql.MySQLLoader;
import lombok.NoArgsConstructor;

@SuppressWarnings("unchecked")
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LoaderFactory {
  public static <O, R extends LoadOption> Loader<O, R> createLoader(LoadType loadType) {
    switch (loadType) {
      case PDF:
        return (Loader<O, R>) PDFLoader.create();
      case SPREADSHEET:
        return (Loader<O, R>) SpreadSheetLoader.create();
      case SQL:
        return (Loader<O, R>) MySQLLoader.create();
      default:
        throw new IllegalArgumentException("Invalid load type: " + loadType);
    }
  }
}
