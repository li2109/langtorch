package ai.knowly.langtorch.utils.graph;

/** Exception thrown when a cycle is detected in the graph. */
public class DAGViolationException extends RuntimeException {
  public DAGViolationException(String message) {
    super(message);
  }
}
