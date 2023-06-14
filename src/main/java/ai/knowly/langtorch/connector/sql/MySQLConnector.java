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

import javax.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/** MySQL loader. */
public class MySQLConnector implements Connector<Records> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final SQLConnectorOption readOption;

  @Inject
  public MySQLConnector(@NonNull SQLConnectorOption readOption) {
    this.readOption = readOption;
  }

  @Override
  public Optional<Records> read() {
    Optional<Connection> newConnection = openConnection(readOption);
    if (!newConnection.isPresent()) {
      logger.atSevere().log("Fail to open connection to MySQL Database");
      return Optional.empty();
    }

    try (Statement stmt = newConnection.get().createStatement()) {
      ResultSet resultSet = stmt.executeQuery(readOption.getQuery());
      return Optional.ofNullable(transform(resultSet));
    } catch (SQLException e) {
      logger.atSevere().withCause(e).log("Error executing query in the MySQL Database");
      return Optional.empty();
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
        logger.atSevere().withCause(e).log("Fail to establish a new connection to database");
        throw new SQLExecutionException("Fail to establish a new connection to database", e);
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
