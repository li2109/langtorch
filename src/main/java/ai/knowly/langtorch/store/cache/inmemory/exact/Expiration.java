package ai.knowly.langtorch.store.cache.inmemory.exact;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Expiration {
  private long time;
  private TimeUnit unit;
}
