package ai.knowly.langtorch.store.cache.inmemory.exact;

import ai.knowly.langtorch.store.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Optional;
import javax.inject.Inject;

/** In memory cache that does exact match on the input */
public class InMemoryExactMatchCache<K, V> implements Cache<K, V> {
  private final com.google.common.cache.Cache<K, V> cache;

  @Inject
  public InMemoryExactMatchCache(InMemoryExactMatchCacheSpec spec) {
    CacheBuilder<Object, Object> cacheBuilder =
        CacheBuilder.newBuilder().maximumSize(spec.getMaximumSize());

    if (spec.getExpireAfterAccess().isPresent()) {
      cacheBuilder.expireAfterAccess(
          spec.getExpireAfterAccess().get().getTime(), spec.getExpireAfterAccess().get().getUnit());
    }
    if (spec.getExpireAfterWrite().isPresent()) {
      cacheBuilder.expireAfterWrite(
          spec.getExpireAfterWrite().get().getTime(), spec.getExpireAfterWrite().get().getUnit());
    }
    this.cache = cacheBuilder.build();
  }

  @Override
  public void put(K key, V value) {
    cache.put(key, value);
  }

  @Override
  public Optional<V> get(K key) {
    return Optional.ofNullable(cache.getIfPresent(key));
  }

  @Override
  public void remove(K key) {
    cache.invalidate(key);
  }

  @Override
  public void clear() {
    cache.invalidateAll();
  }
}
