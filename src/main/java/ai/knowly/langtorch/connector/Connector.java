package ai.knowly.langtorch.connector;

import java.util.Optional;

/** Connector for loading data from a source. */
public interface Connector<T> {

  /**
   * Load data from a source.
   *
   * @return The loaded data.
   */
  Optional<T> read();
}
