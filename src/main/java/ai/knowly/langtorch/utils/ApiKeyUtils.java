package ai.knowly.langtorch.utils;

import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

public class ApiKeyUtils {
  public static void logPartialApiKey(FluentLogger logger, String provider, String apiKey) {
    logger.atInfo().log(
        "Using %s API key: ***************" + apiKey.substring(apiKey.length() - 6), provider);
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

  public static String getPineconeKeyFromEnv() {
    return getPineconeKeyFromEnv(Optional.empty());
  }

  public static String getCohereAIApiKeyFromEnv() {
    return getCohereAIApiKeyFromEnv(Optional.empty());
  }

  public static String getCohereAIApiKeyFromEnv(Optional<FluentLogger> logger) {
    String keyFromEnv = getKeyFromEnv(ApiKey.COHERE_API_KEY);
    logger.ifPresent(l -> logPartialApiKey(l, ApiKey.COHERE_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }

  private static String getKeyFromEnv(ApiKey apiKey) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    String key = dotenv.get(apiKey.name());
    return key;
  }
}
