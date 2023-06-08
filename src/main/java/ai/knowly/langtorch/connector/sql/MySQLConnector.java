package ai.knowly.langtorch.connector.sql;

import com.google.common.flogger.FluentLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/** MySQL connector. */
public class MySQLConnector<S extends StorageObject>
    extends SQLConnector<SQLConnectorOption<S>, S> {
  FluentLogger logger = FluentLogger.forEnclosingClass();
  private Optional<Connection> connection;

  private MySQLConnector(Connection connection) {
    this.connection = Optional.of(connection);
  }

  private MySQLConnector() {
    this.connection = Optional.empty();
  }

  public static <S extends StorageObject> MySQLConnector<S> create(Connection connection) {
    return new MySQLConnector<>(connection);
  }

  public static <S extends StorageObject> MySQLConnector<S> create() {
    return new MySQLConnector<>();
  }

  @Override
  protected S read(SQLConnectorOption<S> readOption) {
    Optional<Connection> newConnection = openConnection(readOption);
    assert newConnection.isPresent();

    try (Statement stmt = connection.get().createStatement()) {
      ResultSet resultSet = stmt.executeQuery(readOption.getQuery());
      return readOption.getStorageObjectTransformFunction().transform(resultSet);
    } catch (SQLException e) {
      logger.atSevere().withCause(e).log("Error executing query in the MySQL Database");
      throw new SQLExecutionException("Error executing query in the MySQL Database", e);
    }
  }

  private Optional<Connection> openConnection(SQLConnectorOption<S> readOption) {
    if (connection.isPresent()) {
      return connection;
    }

    if (!isEligibleForConnection(readOption)) {
      return Optional.empty();
    }

    try {
      return Optional.of(
          DriverManager.getConnection(
              readOption.getQuery(), readOption.getUser().get(), readOption.getPassword().get()));
    } catch (SQLException e) {
      logger.atSevere().withCause(e).log("Error opening a connection to database");
      throw new SQLExecutionException("Error opening a connection to database", e);
    }
  }

  private boolean isEligibleForConnection(SQLConnectorOption<S> readOption) {
    return readOption.getUrl().isPresent()
        && readOption.getUser().isPresent()
        && readOption.getPassword().isPresent();
  }
}
