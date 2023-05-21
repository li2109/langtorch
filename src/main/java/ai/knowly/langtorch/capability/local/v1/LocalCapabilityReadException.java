package ai.knowly.langtorch.capability.local.v1;

import java.io.IOException;

public class LocalCapabilityReadException extends RuntimeException {
  public LocalCapabilityReadException(IOException e) {
    super(e);
  }
}
