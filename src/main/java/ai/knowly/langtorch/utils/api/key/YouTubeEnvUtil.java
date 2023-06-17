package ai.knowly.langtorch.utils.api.key;

import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.getKeyFromEnv;
import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.logPartialApiKey;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import java.util.Optional;

/** Get YouTube key from .env file */

public class YouTubeEnvUtil {
  private YouTubeEnvUtil(){}


  public static String getKey(FluentLogger logger, Environment environment) {
    return getKey(Optional.ofNullable(logger), environment);
  }

  public static String getKey(Environment environment) {
    return getKey(Optional.empty(), environment);
  }

  private static String getKey(Optional<FluentLogger> logger, Environment environment) {
    String keyFromEnv = getKeyFromEnv(KeyType.YOUTUBE_API_KEY, environment);
    logger.ifPresent(l -> logPartialApiKey(l, KeyType.YOUTUBE_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }
}
