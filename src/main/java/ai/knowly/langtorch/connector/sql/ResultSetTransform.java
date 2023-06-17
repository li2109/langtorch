package ai.knowly.langtorch.connector.sql;

import ai.knowly.langtorch.connector.sql.Record.RecordBuilder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

/** Function to transform a ResultSet into a StorageObject. */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ResultSetTransform {
  public static Records transform(ResultSet rs) throws SQLException {
    ResultSetMetaData md = rs.getMetaData();
    int columns = md.getColumnCount();
    List<Record> list = new ArrayList<>();

    while (rs.next()) {
      RecordBuilder recordBuilder = Record.builder();
      for (int i = 1; i <= columns; ++i) {
        recordBuilder.setColumn(md.getColumnName(i), rs.getObject(i));
      }
      list.add(recordBuilder.build());
    }

    return Records.builder().setRows(list).build();
  }
}
