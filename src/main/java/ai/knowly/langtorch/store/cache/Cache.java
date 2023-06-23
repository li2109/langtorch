package ai.knowly.langtorch.store.cache;

import java.util.Optional;

/**
 * A generic cache interface
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public interface Cache<K, V> {
  public void put(K key, V value);

  public Optional<V> get(K key);

  public void remove(K key);

  public void clear();
}
