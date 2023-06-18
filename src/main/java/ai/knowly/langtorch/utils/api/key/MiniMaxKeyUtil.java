package ai.knowly.langtorch.utils.api.key;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;

import java.util.Optional;

import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.getKeyFromEnv;
import static ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils.logPartialApiKey;

/**
 * @author maxiao
 * @date 2023/06/07
 */
/** Get MiniMax key from .env file */
public class MiniMaxKeyUtil {
  private MiniMaxKeyUtil() {}

  public static String getGroupId(Environment environment) {
    return getGroupId(Optional.empty(), environment);
  }

  public static String getGroupId(FluentLogger logger, Environment environment) {
    return getGroupId(Optional.ofNullable(logger), environment);
  }

  private static String getGroupId(Optional<FluentLogger> logger, Environment environment) {
    String groupIdFromEnv = getKeyFromEnv(KeyType.MINMAX_GROUP_ID, environment);
    logger.ifPresent(l -> logPartialApiKey(l, KeyType.MINMAX_GROUP_ID.name(), groupIdFromEnv));
    return groupIdFromEnv;
  }

  public static String getKey(Environment environment) {
    return getKey(Optional.empty(), environment);
  }

  public static String getKey(FluentLogger logger, Environment environment) {
    return getKey(Optional.ofNullable(logger), environment);
  }

  private static String getKey(Optional<FluentLogger> logger, Environment environment) {
    String keyFromEnv = getKeyFromEnv(KeyType.MINIMAX_API_KEY, environment);
    logger.ifPresent(l -> logPartialApiKey(l, KeyType.MINIMAX_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }
}
