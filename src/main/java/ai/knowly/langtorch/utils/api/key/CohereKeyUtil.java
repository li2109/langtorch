package ai.knowly.langtorch.utils.api.key;

import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.getKeyFromEnv;
import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.logPartialApiKey;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import java.util.Optional;

/** Get Cohere key from .env file */
public class CohereKeyUtil {

  private CohereKeyUtil() {}

  public static String getKey(Environment environment) {
    return getKey(Optional.empty(), environment);
  }

  public static String getKey(FluentLogger logger, Environment environment) {
    return getKey(Optional.ofNullable(logger), environment);
  }

  private static String getKey(Optional<FluentLogger> logger, Environment environment) {
    String endpointFromEnv = getKeyFromEnv(KeyType.COHERE_API_KEY, environment);
    logger.ifPresent(l -> logPartialApiKey(l, KeyType.COHERE_API_KEY.name(), endpointFromEnv));
    return endpointFromEnv;
  }
}
