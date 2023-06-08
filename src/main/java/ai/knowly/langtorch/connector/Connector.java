package ai.knowly.langtorch.connector;

import java.io.IOException;

/** Connector for loading data from a source. */
public abstract class Connector<O, R extends ConnectorOption> {
  protected abstract O read(R loadOption) throws IOException;
}
