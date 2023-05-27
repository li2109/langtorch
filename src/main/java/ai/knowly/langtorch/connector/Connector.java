package ai.knowly.langtorch.connector;

import java.io.IOException;

/** Connector for reading data from a source. */
public abstract class Connector<I, O> {
  protected abstract O read(I source) throws IOException;
}
