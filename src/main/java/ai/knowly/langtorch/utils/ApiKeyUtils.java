package ai.knowly.langtorch.utils;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.FluentLogger.Api;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;

public class ApiKeyUtils {
  private ApiKeyUtils() {}

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

  public static String getYoutubeApiKeyFromEnv(){
    return getYoutubeApiKeyFromEnv(Optional.empty());
  }

  public static String getYoutubeApiKeyFromEnv(Optional<FluentLogger> logger){
    String keyFromEnv = getKeyFromEnv(ApiKey.YOUTUBE_API_KEY);
    logger.ifPresent(l -> logPartialApiKey(l, ApiKey.YOUTUBE_API_KEY.name(), keyFromEnv));
    return keyFromEnv;
  }

  public static String getGoogleSecretsFromEnv(){
    return getGoogleSecretsFromEnv(Optional.empty());
  }

  public static String getGoogleSecretsFromEnv(Optional<FluentLogger> logger){
    String keyFromEnv = getKeyFromEnv(ApiKey.GOOGLE_SECRETS);
    logger.ifPresent(l -> logPartialApiKey(l, ApiKey.GOOGLE_SECRETS.name(), keyFromEnv));
    return keyFromEnv;
  }


  private static String getKeyFromEnv(ApiKey apiKey) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    return dotenv.get(apiKey.name());
  }
}
