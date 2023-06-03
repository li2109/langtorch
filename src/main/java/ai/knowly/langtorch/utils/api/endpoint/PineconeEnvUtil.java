package ai.knowly.langtorch.utils.api.endpoint;

import static ai.knowly.langtorch.utils.api.endpoint.EndpointUtil.logEndPoint;

import ai.knowly.langtorch.utils.Environment;
import com.google.common.flogger.FluentLogger;
import java.util.Optional;

/** Get Pinecone endpoint from .env file */
public class PineconeEnvUtil {
  private PineconeEnvUtil() {}

  public static String getEndPoint(Environment environment) {
    return getPineconeEndPointFromEnv(Optional.empty(), environment);
  }

  public static String getEndPoint(FluentLogger logger, Environment environment) {
    return getPineconeEndPointFromEnv(Optional.ofNullable(logger), environment);
  }

  private static String getPineconeEndPointFromEnv(
      Optional<FluentLogger> logger, Environment environment) {
    String endpointFromEnv =
        EndpointUtil.getEndPoint(VectorStoreApiEndpoint.PINECONE_ENDPOINT, environment);
    logger.ifPresent(
        l -> logEndPoint(l, VectorStoreApiEndpoint.PINECONE_ENDPOINT.name(), endpointFromEnv));
    return endpointFromEnv;
  }
}
