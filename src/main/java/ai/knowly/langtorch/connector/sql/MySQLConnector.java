package ai.knowly.langtorch.connector.sql;

import com.google.common.flogger.FluentLogger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** MySQL connector. */
public class MySQLConnector<S extends StorageObject> extends SQLConnector<SQLReadOption<S>, S> {
  private final Connection connection;
  FluentLogger logger = FluentLogger.forEnclosingClass();

  private MySQLConnector(Connection connection) {
    this.connection = connection;
  }

  public static <S extends StorageObject> MySQLConnector<S> create(Connection connection) {
    return new MySQLConnector<>(connection);
  }

  @Override
  protected S read(SQLReadOption<S> readOption) {
    try (Statement stmt = connection.createStatement()) {
      ResultSet resultSet = stmt.executeQuery(readOption.getQuery());
      return readOption.getStorageObjectTransformFunction().transform(resultSet);
    } catch (SQLException e) {
      logger.atSevere().withCause(e).log("Error executing query in the MySQL Database");
      throw new SQLExecutionException("Error executing query in the MySQL Database", e);
    }
  }
}
