package ai.knowly.langtorch.utils.api.key;

import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.getKeyFromEnv;
import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.logPartialApiKey;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import java.util.Optional;

/** Get Google Secret from .env file */

public class GoogleSecretUtil {
  private GoogleSecretUtil(){}
  public static String getKey(FluentLogger logger, Environment environment) {
    return getKey(Optional.ofNullable(logger), environment);
  }

  public static String getKey(Environment environment) {
    return getKey(Optional.empty(), environment);
  }
  private static String getKey(Optional<FluentLogger> logger, Environment environment) {
    String keyFromEnv = getKeyFromEnv(KeyType.GOOGLE_SECRETS, environment);
    logger.ifPresent(l -> logPartialApiKey(l, KeyType.GOOGLE_SECRETS.name(), keyFromEnv));
    return keyFromEnv;
  }
}
