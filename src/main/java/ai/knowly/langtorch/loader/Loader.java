package ai.knowly.langtorch.loader;

import java.io.IOException;

/** Connector for loading data from a source. */
public abstract class Loader<O, R extends LoadOption> {
  protected abstract O read(R loadOption) throws IOException;
}
