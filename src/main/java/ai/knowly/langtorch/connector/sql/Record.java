package ai.knowly.langtorch.connector.sql;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(toBuilder = true, setterPrefix = "set")
public class Record {
  @Singular("column")
  Map<String, Object> columns;

  public Optional<Object> getColumn(String columnName) {
    if (!columns.containsKey(columnName)) {
      return Optional.empty();
    }
    return Optional.of(columns.get(columnName));
  }
}
