package ai.knowly.langtorch.connector;

import java.io.IOException;

/** Connector for reading data from a source. */
public abstract class Connector<O, R extends ReadOption> {
  protected abstract O read(R readOption) throws IOException;
}
