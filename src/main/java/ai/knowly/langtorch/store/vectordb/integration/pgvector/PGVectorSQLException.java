package ai.knowly.langtorch.store.vectordb.integration.pgvector;

import java.sql.SQLException;

public class PGVectorSQLException extends RuntimeException {
  public PGVectorSQLException(SQLException e) {
    super(e);
  }
}
