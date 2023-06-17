package ai.knowly.langtorch.connector.sql;

import static com.google.common.truth.Truth.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MySQLConnectorTest {

  private Connection conn;

  @BeforeEach
  void setUp() {
    // Initialize the database
    try {
      Connection conn =
          DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
      this.conn = conn;
      Statement stmt = conn.createStatement();

      // Drop the table if it exists
      stmt.execute("DROP TABLE IF EXISTS TEST_TABLE");
      stmt.execute("CREATE TABLE test_table(id INT PRIMARY KEY, name VARCHAR(255))");
      stmt.execute("INSERT INTO test_table(id, name) VALUES(1, 'Test')");
    } catch (SQLException e) {
      throw new RuntimeException("Error initializing database", e);
    }
  }

  @Test
  void testRead() {
    MySQLConnector loader =
        new MySQLConnector(
            SQLConnectorOption.builder()
                .setQuery("SELECT * FROM test_table WHERE id = 1")
                .setConnection(conn)
                .build());

    Optional<Records> records = loader.read();

    assertThat(records.isPresent()).isTrue();
    assertThat(records.get().getRows().get(0).getColumn("ID").get()).isEqualTo(1);
    assertThat(records.get().getRows().get(0).getColumn("NAME").get()).isEqualTo("Test");
  }

  @Test
  void testRead_NoResult() {
    MySQLConnector loader =
        new MySQLConnector(
            SQLConnectorOption.builder()
                .setQuery("SELECT * FROM test_table WHERE id = 2")
                .setConnection(conn)
                .build());

    Optional<Records> records = loader.read();

    assertThat(records.get().getRows()).isEmpty();
  }

  @Test
  void testRead_MultipleResults() throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.execute("INSERT INTO test_table(id, name) VALUES(2, 'Test2')");
    stmt.execute("INSERT INTO test_table(id, name) VALUES(3, 'Test3')");

    MySQLConnector loader =
        new MySQLConnector(
            SQLConnectorOption.builder()
                .setQuery("SELECT * FROM test_table")
                .setConnection(conn)
                .build());

    Optional<Records> records = loader.read();

    assertThat(records.get().getRows().get(0).getColumn("ID").get()).isEqualTo(1);
    assertThat(records.get().getRows().get(0).getColumn("NAME").get()).isEqualTo("Test");
  }

  @Test
  void testRead_emptyRecord() {
    MySQLConnector connector =
        new MySQLConnector(
            SQLConnectorOption.builder()
                .setQuery("SELECT * FROM non_existent_table")
                .setConnection(conn)
                .build());

    Optional<Records> records = connector.read();
    assertThat(records.isPresent()).isFalse();
  }
}
