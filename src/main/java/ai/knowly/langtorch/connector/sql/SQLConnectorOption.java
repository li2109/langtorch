package ai.knowly.langtorch.connector.sql;

import ai.knowly.langtorch.connector.ConnectorOption;
import java.sql.Connection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SQLConnectorOption implements ConnectorOption {
  @NonNull private String query;
  private Connection connection;
  private ConnectionDetail connectionDetail;

  public Optional<Connection> getConnection() {
    return Optional.ofNullable(connection);
  }

  public Optional<ConnectionDetail> getConnectionDetail() {
    return Optional.ofNullable(connectionDetail);
  }
}
