package ai.knowly.langtorch.prompt.manager;

import java.io.IOException;

public class FileLoadingException extends RuntimeException {
  public FileLoadingException(IOException e) {
    super(e);
  }
}
