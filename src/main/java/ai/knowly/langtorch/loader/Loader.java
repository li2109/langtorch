package ai.knowly.langtorch.loader;

import java.util.Optional;

/** Connector for loading data from a source. */
public interface Loader<T> {

  /**
   * Load data from a source.
   *
   * @return The loaded data.
   */
  Optional<T> read();
}
