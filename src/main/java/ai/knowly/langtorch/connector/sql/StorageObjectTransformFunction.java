package ai.knowly.langtorch.connector.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface StorageObjectTransformFunction<S extends StorageObject> {
  S transform(ResultSet resultSet) throws SQLException;
}
