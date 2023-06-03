package ai.knowly.langtorch.loader.sql;

import com.google.common.flogger.FluentLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/** MySQL loader. */
public class MySQLLoader<S extends StorageObject> extends SQLLoader<SQLLoadOption<S>, S> {
  FluentLogger logger = FluentLogger.forEnclosingClass();
  private Optional<Connection> connection;

  private MySQLLoader(Connection connection) {
    this.connection = Optional.of(connection);
  }

  private MySQLLoader() {
    this.connection = Optional.empty();
  }

  public static <S extends StorageObject> MySQLLoader<S> create(Connection connection) {
    return new MySQLLoader<>(connection);
  }

  public static <S extends StorageObject> MySQLLoader<S> create() {
    return new MySQLLoader<>();
  }

  @Override
  protected S read(SQLLoadOption<S> readOption) {
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

  private Optional<Connection> openConnection(SQLLoadOption<S> readOption) {
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

  private boolean isEligibleForConnection(SQLLoadOption<S> readOption) {
    return readOption.getUrl().isPresent()
        && readOption.getUser().isPresent()
        && readOption.getPassword().isPresent();
  }
}
