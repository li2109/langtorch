package ai.knowly.langtorch.store.vectordb.integration.pgvector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * This class provides SQL commands for creating and querying the embeddings and metadata tables in
 * a PostgreSQL database.
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SqlCommandProvider {

  /** The name of the database that the embeddings and metadata tables will be created in. */
  @NonNull private final String databaseName;

  /**
   * Whether or not to overwrite the existing embeddings and metadata tables if they already exist.
   */
  private final boolean overwrite;

  /**
   * Returns a SQL query that will create the embeddings table.
   *
   * @param vectorDimensions The number of dimensions in the embeddings.
   * @return The SQL query.
   */
  public String getCreateEmbeddingsTableQuery(int vectorDimensions) {
    if (vectorDimensions <= 0) {
      throw new IllegalArgumentException(
          "vectorDimensions must be greater than 0, was " + vectorDimensions);
    }
    String query = "";
    if (overwrite) {
      query += "DROP TABLE IF EXISTS " + getEmbeddingsTableName() + " CASCADE; ";
    }
    query += "CREATE TABLE IF NOT EXISTS ";

    query +=
        getEmbeddingsTableName()
            + " ("
            + "id TEXT PRIMARY KEY, "
            + "embedding vector("
            + vectorDimensions
            + ")"
            + ")";

    return query;
  }

  /**
   * Returns a SQL query that will create the metadata table.
   *
   * @return The SQL query.
   */
  public String getCreateMetadataTableQuery() {
    String query = "";
    if (overwrite) {
      query += "DROP TABLE IF EXISTS " + getMetadataTableName() + "; ";
    }
    query += "CREATE TABLE IF NOT EXISTS ";

    query +=
        getMetadataTableName()
            + " ("
            + "id TEXT PRIMARY KEY, "
            + // vectorId + key
            "key TEXT, "
            + "value TEXT ,"
            + "vector_id TEXT ,"
            + "FOREIGN KEY (vector_id) REFERENCES "
            + getEmbeddingsTableName()
            + "(id)"
            + ")";

    return query;
  }

  /**
   * Returns a SQL query that will insert a new row into the embeddings table.
   *
   * @param parameters The parameters for the insert statement.
   * @return The SQL query.
   */
  public String getInsertEmbeddingsQuery(String parameters) {
    return "INSERT INTO "
        + getEmbeddingsTableName()
        + " "
        + "(id, embedding) "
        + "VALUES "
        + parameters;
  }

  /**
   * Returns a SQL query that will insert a new row into the metadata table.
   *
   * @param parameters The parameters for the insert statement.
   * @return The SQL query.
   */
  public String getInsertMetadataQuery(String parameters) {
    return "INSERT INTO "
        + getMetadataTableName()
        + " "
        + "(id, key, value, vector_id) "
        + "VALUES "
        + parameters;
  }

  /**
   * Returns a SQL query that will select a subset of the embeddings and metadata rows.
   *
   * @param distanceStrategy The distance strategy to use when ordering the results.
   * @param limit The maximum number of rows to return.
   * @return The SQL query.
   */
  public String getSelectEmbeddingsQuery(String distanceStrategy, long limit) {
    return "SELECT "
        + getEmbeddingsTableName()
        + ".id, embedding, key, value FROM "
        + "("
        + "SELECT "
        + getEmbeddingsTableName()
        + ".id, embedding "
        + "FROM "
        + getEmbeddingsTableName()
        + " "
        + "LIMIT "
        + limit
        + " "
        + ") AS "
        + getEmbeddingsTableName()
        + " "
        + "LEFT JOIN "
        + getMetadataTableName()
        + " ON "
        + getEmbeddingsTableName()
        + ".id = "
        + getMetadataTableName()
        + ".vector_id "
        + "ORDER BY embedding "
        + distanceStrategy
        + " ? ";
  }

  private String getEmbeddingsTableName() {
    return databaseName + "_embeddings";
  }

  private String getMetadataTableName() {
    return getEmbeddingsTableName() + "_metadata";
  }
}
