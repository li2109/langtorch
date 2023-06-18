package ai.knowly.langtorch.utils.api.key;

import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.getKeyFromEnv;
import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.logPartialApiKey;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import java.util.Optional;

/** Get OpenAI key from .env file */
public class OpenAIKeyUtil {
  private OpenAIKeyUtil() {}

  public static String getKey(Environment environment) {
    return getKey(Optional.empty(), environment);
  }

  public static String getKey(FluentLogger logger, Environment environment) {
    return getKey(Optional.ofNullable(logger), environment);
  }

  private static String getKey(Optional<FluentLogger> logger, Environment environment) {
    String keyFromEnv = getKeyFromEnv(KeyType.OPENAI_API_KEY, environment);
    logger.ifPresent(l -> logPartialApiKey(l, KeyType.OPENAI_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }
}
