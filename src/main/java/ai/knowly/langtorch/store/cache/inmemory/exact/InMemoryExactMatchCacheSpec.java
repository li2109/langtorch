package ai.knowly.langtorch.store.cache.inmemory.exact;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Specification for the in memory exact match cache */
@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InMemoryExactMatchCacheSpec {
  public static InMemoryExactMatchCacheSpec getDefaultInstance() {
    return InMemoryExactMatchCacheSpec.builder().build();
  }

  @Builder.Default private long maximumSize = 1000;
  private Expiration expireAfterWrite;
  private Expiration expireAfterAccess;

  public Optional<Expiration> getExpireAfterWrite() {
    return Optional.ofNullable(expireAfterWrite);
  }

  public Optional<Expiration> getExpireAfterAccess() {
    return Optional.ofNullable(expireAfterAccess);
  }
}
