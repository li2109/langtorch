package ai.knowly.langtorch.connector.markdown;

/** Exception thrown when a Markdown file cannot be read. */
public class MarkdownReadException extends RuntimeException {
  public MarkdownReadException(Exception e) {
    super(e);
  }
}
