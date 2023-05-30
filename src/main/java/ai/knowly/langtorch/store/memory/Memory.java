package ai.knowly.langtorch.store.memory;

import java.util.List;

/** Interface for a generic memory structure. */
public interface Memory<V extends MemoryValue, C extends MemoryContext> {
  /**
   * Adds a value to the memory.
   *
   * @param value the value
   */
  void add(V value);

  /** Retrieves all values added into the memory. */
  List<V> getAll();

  /** Removes all values from the memory. */
  void clear();

  /** Returns the context based on entries in the memory. */
  C getMemoryContext();
}
