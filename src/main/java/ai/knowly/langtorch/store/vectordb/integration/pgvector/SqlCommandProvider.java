package ai.knowly.langtorch.store.vectordb.integration.pgvector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SqlCommandProvider {

    @NonNull
    private final String databaseName;

    public String getCreateEmbeddingsTableQuery(int vectorDimensions) {
        assert vectorDimensions > 0 : "vectorDimensions must be greater than 0, was " + vectorDimensions;
        return "CREATE TABLE IF NOT EXISTS " + getEmbeddingsTableName() + " (" +
                "id TEXT PRIMARY KEY ," +
                "embedding vector(" + vectorDimensions + ")" +
                ")";
    }

    public String getCreateMetadataTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + getMetadataTableName() + " (" +
                "id TEXT PRIMARY KEY ," + //vectorId + key
                "key TEXT, " +
                "value TEXT ," +
                "vector_id TEXT ," +
                "FOREIGN KEY (vector_id) REFERENCES " + getEmbeddingsTableName() + "(id)" +
                ")";
    }

    public String getInsertEmbeddingsQuery(String parameters) {
        return "INSERT INTO " + getEmbeddingsTableName() + " " +
                "(id, embedding) " +
                "VALUES " + parameters;
    }

    public String getInsertMetadataQuery(String parameters) {
        return "INSERT INTO " + getMetadataTableName() + " " +
                "(id, key, value, vector_id) " +
                "VALUES " + parameters;
    }

    public String getSelectEmbeddingsQuery(String distanceStrategy, long limit) {
        return "SELECT " + getEmbeddingsTableName() + ".id, embedding, key, value FROM " +
                "(" +
                "SELECT " + getEmbeddingsTableName() + ".id, embedding " +
                "FROM " + getEmbeddingsTableName() + " " +
                "LIMIT " + limit + " " +
                ") AS " + getEmbeddingsTableName() + " " +
                "LEFT JOIN " + getMetadataTableName() + " ON " +
                getEmbeddingsTableName() + ".id = " + getMetadataTableName() + ".vector_id " +
                "ORDER BY embedding " + distanceStrategy + " ? ";
    }

    private String getEmbeddingsTableName() {
        return databaseName + "_embeddings";
    }

    private String getMetadataTableName() {
        return getEmbeddingsTableName() + "_metadata";
    }

}
