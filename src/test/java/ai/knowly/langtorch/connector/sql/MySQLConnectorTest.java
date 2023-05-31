package ai.knowly.langtorch.connector.sql;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MySQLConnectorTest {
  private static final StorageObjectTransformFunction<TestStorageObject> EXTRACT_TEST_STORAGE_FUNC =
      resultSet -> {
        if (resultSet.next()) {
          return new TestStorageObject(resultSet.getInt("id"), resultSet.getString("name"));
        } else {
          return TestStorageObject.EMPTY;
        }
      };
  private MySQLConnector<TestStorageObject> connector;
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

      // Connect to the in-memory H2 database for testing
      connector = MySQLConnector.create(conn);
    } catch (SQLException e) {
      throw new RuntimeException("Error initializing database", e);
    }
  }

  @Test
  void testRead() {
    SQLReadOption<TestStorageObject> readOption =
        new SQLReadOption<>("SELECT * FROM test_table WHERE id = 1", EXTRACT_TEST_STORAGE_FUNC);

    TestStorageObject objectOptional = connector.read(readOption);

    assertThat(objectOptional.getId()).isEqualTo(1);
    assertThat(objectOptional.getName()).isEqualTo("Test");
  }

  @Test
  void testRead_NoResult() {
    SQLReadOption<TestStorageObject> readOption =
        new SQLReadOption<>("SELECT * FROM test_table WHERE id = 2", EXTRACT_TEST_STORAGE_FUNC);

    TestStorageObject objectOptional = connector.read(readOption);

    assertThat(objectOptional).isEqualTo(TestStorageObject.EMPTY);
  }

  @Test
  void testRead_MultipleResults() throws SQLException {
    Statement stmt = conn.createStatement();
    stmt.execute("INSERT INTO test_table(id, name) VALUES(2, 'Test2')");
    stmt.execute("INSERT INTO test_table(id, name) VALUES(3, 'Test3')");

    SQLReadOption<TestStorageObject> readOption =
        new SQLReadOption<>("SELECT * FROM test_table", EXTRACT_TEST_STORAGE_FUNC);

    TestStorageObject objectOptional = connector.read(readOption);

    assertThat(objectOptional.getId()).isEqualTo(1);
    assertThat(objectOptional.getName()).isEqualTo("Test");
  }

  @Test
  void testRead_SQLException() {
    SQLReadOption<TestStorageObject> readOption =
        new SQLReadOption<>("SELECT * FROM non_existent_table", EXTRACT_TEST_STORAGE_FUNC);

    assertThrows(RuntimeException.class, () -> connector.read(readOption));
  }

  private static class TestStorageObject implements StorageObject {
    public static final TestStorageObject EMPTY = new TestStorageObject();
    private int id;
    private String name;

    public TestStorageObject(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public TestStorageObject() {}

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }
}
