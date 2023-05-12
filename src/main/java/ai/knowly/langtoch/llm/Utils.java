package ai.knowly.langtoch.llm;

import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.Single;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Utils {
  public static void logPartialOpenAIApiKey(FluentLogger logger, String apiKey) {
    logger.atInfo().log(
        "Using OpenAI API key: ***************" + apiKey.substring(apiKey.length() - 6));
  }

  public static String getOpenAIApiKeyFromEnv(FluentLogger logger) {
    Dotenv dotenv = Dotenv.load();
    String openaiApiKey = Objects.requireNonNull(dotenv.get("OPENAI_API_KEY"));
    Utils.logPartialOpenAIApiKey(logger, openaiApiKey);
    return openaiApiKey;
  }

  public static <T> CompletableFuture<T> singleToCompletableFuture(Single<T> single) {
    CompletableFuture<T> future = new CompletableFuture<>();
    single.subscribe(future::complete, future::completeExceptionally);
    return future;
  }
}
