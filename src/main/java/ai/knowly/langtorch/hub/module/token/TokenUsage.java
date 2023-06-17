package ai.knowly.langtorch.hub.module.token;

import java.util.concurrent.atomic.AtomicLong;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUsage {
  @Builder.Default private AtomicLong promptTokenUsage = new AtomicLong(0);
  @Builder.Default private AtomicLong completionTokenUsage = new AtomicLong(0);
}
