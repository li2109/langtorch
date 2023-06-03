package ai.knowly.langtorch.utils.api.key;

import static ai.knowly.langtorch.utils.Constants.TEST_RESOURCE_FOLDER;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;

public class ApiKeyEnvUtils {
  private ApiKeyEnvUtils() {}

  static void logPartialApiKey(FluentLogger logger, String provider, String apiKey) {
    logger.atInfo().log(
        "Using %s API key: ***************" + apiKey.substring(apiKey.length() - 6), provider);
  }

  static String getKeyFromEnv(KeyType keyType, Environment environment) {
    Dotenv dotenv;
    if (environment == Environment.PRODUCTION) {
      dotenv = Dotenv.configure().ignoreIfMissing().load();
    } else {
      dotenv = Dotenv.configure().directory(TEST_RESOURCE_FOLDER).ignoreIfMissing().load();
    }
    String key = dotenv.get(keyType.name());
    if (key == null) {
      throw new KeyNotFoundException(
          String.format(
              "Could not find %s in .env file. Please add it to the .env file.", keyType.name()));
    }
    return key;
  }
}
