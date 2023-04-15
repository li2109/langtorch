package ai.knowly.langtoch.memory;

import java.util.Map;
import java.util.Optional;

/**
 * Abstract class for a generic memory structure with key-value pairs.
 *
 * @param <K> the type of keys maintained by this memory
 * @param <Y> the type of values maintained by this memory
 */
public abstract class Memory<K, Y> {
  /**
   * Adds a key-value pair to the memory.
   *
   * @param key the key to add
   * @param value the value to associate with the key
   */
  public abstract void add(K key, Y value);

  /**
   * Retrieves the value associated with the specified key, if present.
   *
   * @param key the key whose associated value is to be returned
   * @return an Optional containing the value associated with the key, or an empty Optional if the
   *     key is not present
   */
  public abstract Optional<Y> get(K key);

  /**
   * Removes the key-value pair associated with the specified key.
   *
   * @param key the key whose associated key-value pair is to be removed
   */
  public abstract void remove(K key);

  /** Removes all key-value pairs from the memory. */
  public abstract void clear();

  /**
   * Returns the memory.
   *
   * @return the memory
   */
  public abstract Map<K, Y> getMemory();
}
