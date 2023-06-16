package ai.knowly.langtorch.store.vectordb.integration.pgvector;

import com.pgvector.PGvector;

import java.sql.*;

public class PGVectorService {

    private final Connection connection;
    private Statement defaultStatement = null;

    public PGVectorService(Connection connection) {
        this.connection = connection;
        init();
    }

    private void init() {
        try {
            PGvector.addVectorType(connection);
            defaultStatement = connection.createStatement();
            defaultStatement.executeUpdate("CREATE EXTENSION IF NOT EXISTS vector");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        return defaultStatement.executeUpdate(sql);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public ResultSet query(String sql) throws SQLException {
        return defaultStatement.executeQuery(sql);
    }

}
