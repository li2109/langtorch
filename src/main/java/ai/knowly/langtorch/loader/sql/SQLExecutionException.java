package ai.knowly.langtorch.loader.sql;

/** Exception thrown when an error occurs during SQL execution. */
public class SQLExecutionException extends RuntimeException {
  public SQLExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
}
