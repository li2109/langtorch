package ai.knowly.langtorch.utils.future.retry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RetryConfig {
  @Builder.Default private int maxRetries = 2;
  @Builder.Default private long retryIntervalMillis = 200;

  public static RetryConfig getDefaultInstance() {
    return RetryConfig.builder().build();
  }
}
