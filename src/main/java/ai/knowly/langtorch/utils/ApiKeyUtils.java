package ai.knowly.langtorch.utils;

import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

public class ApiKeyUtils {
  private ApiKeyUtils() {}

  public static void logPartialApiKey(FluentLogger logger, String provider, String apiKey) {
    logger.atInfo().log(
        "Using %s API key: ***************" + apiKey.substring(apiKey.length() - 6), provider);
  }

  public static void logEndPoint(FluentLogger logger, String provider, String endpoint) {
    logger.atInfo().log("Using %s endpoint: ***************" + endpoint);
  }

  public static String getOpenAIApiKeyFromEnv() {
    return getOpenAIApiKeyFromEnv(Optional.empty());
  }

  public static String getOpenAIApiKeyFromEnv(Optional<FluentLogger> logger) {
    String keyFromEnv = getKeyFromEnv(ApiKey.OPENAI_API_KEY);
    logger.ifPresent(l -> logPartialApiKey(l, ApiKey.OPENAI_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }

  public static String getPineconeKeyFromEnv(Optional<FluentLogger> logger) {
    String keyFromEnv = getKeyFromEnv(ApiKey.PINECONE_API_KEY);
    logger.ifPresent(l -> logPartialApiKey(l, ApiKey.PINECONE_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }

  public static String getPineconeEndPointFromEnv(Optional<FluentLogger> logger) {
    String endpointFromEnv = getEndPointFromEnv(ApiEndpoint.PINECONE_ENDPOINT);
    logger.ifPresent(l -> logEndPoint(l, ApiEndpoint.PINECONE_ENDPOINT.name(), endpointFromEnv));
    return endpointFromEnv;
  }

  public static String getPineconeKeyFromEnv() {
    return getPineconeKeyFromEnv(Optional.empty());
  }

  public static String getCohereAIApiKeyFromEnv() {
    return getCohereAIApiKeyFromEnv(Optional.empty());
  }

  public static String getCohereAIApiKeyFromEnv(Optional<FluentLogger> logger) {
    String endpointFromEnv = getKeyFromEnv(ApiKey.COHERE_API_KEY);
    logger.ifPresent(l -> logPartialApiKey(l, ApiKey.COHERE_API_KEY.name(), endpointFromEnv));
    return endpointFromEnv;
  }

  private static String getKeyFromEnv(ApiKey apiKey) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    return dotenv.get(apiKey.name());
  }

  private static String getEndPointFromEnv(ApiEndpoint apiEndpoint) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    return dotenv.get(apiEndpoint.name());
  }
}
