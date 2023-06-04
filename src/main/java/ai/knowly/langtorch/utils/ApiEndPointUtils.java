package ai.knowly.langtorch.utils;

import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Optional;

public class ApiEndPointUtils {
  private ApiEndPointUtils() {}

  public static void logEndPoint(FluentLogger logger, String provider, String endpoint) {
    logger.atInfo().log("Using %s endpoint: ***************" + endpoint);
  }

  public static String getPineconeEndPointFromEnv(Optional<FluentLogger> logger) {
    String endpointFromEnv =
        getVectorStoreEndpointFromEnv(VectorStoreApiEndpoint.PINECONE_ENDPOINT);
    logger.ifPresent(
        l -> logEndPoint(l, VectorStoreApiEndpoint.PINECONE_ENDPOINT.name(), endpointFromEnv));
    return endpointFromEnv;
  }

  private static String getVectorStoreEndpointFromEnv(
      VectorStoreApiEndpoint vectorStoreApiEndpoint) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    return dotenv.get(vectorStoreApiEndpoint.name());
  }
}
