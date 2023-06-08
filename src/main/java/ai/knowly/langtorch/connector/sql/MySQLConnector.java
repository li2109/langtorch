package ai.knowly.langtorch.connector.sql;

import ai.knowly.langtorch.connector.Connector;
import com.google.common.flogger.FluentLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/** MySQL connector. */
import static ai.knowly.langtorch.connector.sql.ResultSetTransform.transform;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/** MySQL loader. */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MySQLConnector implements Connector<Records> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  @NonNull private SQLConnectorOption readOption;

  public static MySQLConnector create(SQLConnectorOption readOption) {
    return new MySQLConnector(readOption);
  }

  @Override
  public Optional<Records> read() {
    Optional<Connection> newConnection = openConnection(readOption);
    if (!newConnection.isPresent()) {
      logger.atSevere().log("Error opening a connection to database");
      return Optional.empty();
    }

    try (Statement stmt = newConnection.get().createStatement()) {
      ResultSet resultSet = stmt.executeQuery(readOption.getQuery());
      return Optional.ofNullable(transform(resultSet));
    } catch (SQLException e) {
      logger.atSevere().withCause(e).log("Error executing query in the MySQL Database");
      throw new SQLExecutionException("Error executing query in the MySQL Database", e);
    }
  }

  private Optional<Connection> openConnection(SQLConnectorOption readOption) {
    if (readOption.getConnection().isPresent()) {
      return readOption.getConnection();
    }

    if (!isEligibleForConnection(readOption)) {
      try {
        return Optional.of(
            DriverManager.getConnection(
                readOption.getConnectionDetail().get().getUrl().get(),
                readOption.getConnectionDetail().get().getUser().get(),
                readOption.getConnectionDetail().get().getPassword().get()));
      } catch (SQLException e) {
        logger.atSevere().withCause(e).log("Error opening a connection to database");
        throw new SQLExecutionException("Error opening a connection to database", e);
      }
    }
    return Optional.empty();
  }

  private boolean isEligibleForConnection(SQLConnectorOption readOption) {
    return readOption.getConnectionDetail().isPresent()
        && readOption.getConnectionDetail().get().getUrl().isPresent()
        && readOption.getConnectionDetail().get().getUser().isPresent()
        && readOption.getConnectionDetail().get().getPassword().isPresent();
  }
}
