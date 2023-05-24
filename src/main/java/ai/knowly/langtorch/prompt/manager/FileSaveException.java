package ai.knowly.langtorch.prompt.manager;

public class FileSaveException extends RuntimeException {
  public FileSaveException(Exception e) {
    super(e);
  }
}
