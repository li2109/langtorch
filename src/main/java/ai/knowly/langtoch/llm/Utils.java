package ai.knowly.langtoch.llm;

import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.Single;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Utils {
  public static void logPartialApiKey(FluentLogger logger, String provider, String apiKey) {
    logger.atInfo().log(
        "Using %s API key: ***************" + apiKey.substring(apiKey.length() - 6), provider);
  }

  public static String getOpenAIApiKeyFromEnv() {
    return getOpenAIApiKeyFromEnv(Optional.empty());
  }

  public static String getOpenAIApiKeyFromEnv(Optional<FluentLogger> logger) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    String openaiApiKey = dotenv.get("OPENAI_API_KEY");
    logger.ifPresent(l -> logPartialApiKey(l, "OpenAI", openaiApiKey));
    return openaiApiKey;
  }

  public static String getCohereAIApiKeyFromEnv() {
    return getCohereAIApiKeyFromEnv(Optional.empty());
  }

  public static String getCohereAIApiKeyFromEnv(Optional<FluentLogger> logger) {
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    String openaiApiKey = dotenv.get("COHERE_API_KEY");
    logger.ifPresent(l -> logPartialApiKey(l, "CohereAI", openaiApiKey));
    return openaiApiKey;
  }

  public static <T> CompletableFuture<T> singleToCompletableFuture(Single<T> single) {
    CompletableFuture<T> future = new CompletableFuture<>();
    single.subscribe(future::complete, future::completeExceptionally);
    return future;
  }
}
