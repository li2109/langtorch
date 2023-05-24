package ai.knowly.langtorch.store.memory;

import com.google.common.collect.Multimap;
import java.util.List;

/**
 * Interface for a generic memory structure with key-value pairs.
 *
 * @param <K> the type of keys maintained by this memory
 * @param <Y> the type of values maintained by this memory
 */
public interface Memory<K, Y> {
  /**
   * Adds a key-value pair to the memory.
   *
   * @param key the key to add
   * @param value the value to associate with the key
   */
  void add(K key, Y value);

  /**
   * Retrieves values associated with the specified key, if present.
   *
   * @param key the key whose associated value is to be returned
   * @return List of values associated with the key
   */
  List<Y> get(K key);

  /** Removes all key-value pairs from the memory. */
  void clear();

  /**
   * Returns the memory.
   *
   * @return the memory
   */
  Multimap<K, Y> getMemory();

  /** Returns the prompt based on entries in the memory. */
  String getMemoryContext();
}
