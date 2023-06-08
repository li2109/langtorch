package ai.knowly.langtorch.connector.sql;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Builder(toBuilder = true, setterPrefix = "set")
public class Records {
  @Singular("row")
  List<Record> rows;
}
