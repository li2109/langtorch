package ai.knowly.langtorch.utils.api.endpoint;

import static ai.knowly.langtorch.utils.Constants.TEST_RESOURCE_FOLDER;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;

/** Utility class for getting endpoints from .env file */
public class endpointUtil {
  private endpointUtil() {}

  public static String getEndPoint(VectorStoreApiEndpoint apiEndpoint, Environment environment) {
    Dotenv dotenv;
    if (environment == Environment.PRODUCTION) {
      dotenv = Dotenv.configure().ignoreIfMissing().load();
    } else {
      dotenv = Dotenv.configure().directory(TEST_RESOURCE_FOLDER).ignoreIfMissing().load();
    }
    return dotenv.get(apiEndpoint.name());
  }

  public static void logEndPoint(FluentLogger logger, String provider, String endpoint) {
    logger.atInfo().log("Using %s endpoint: %s", provider, endpoint);
  }
}
