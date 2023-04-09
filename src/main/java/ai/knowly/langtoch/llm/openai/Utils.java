package ai.knowly.langtoch.llm.openai;

import com.google.common.flogger.FluentLogger;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;

public class Utils {
  public static void logPartialApiKey(FluentLogger logger, String apiKey) {
    logger.atInfo().log(
        "Using OpenAI API key: ***************" + apiKey.substring(apiKey.length() - 6));
  }

  public static String getApiKeyFromEnv(FluentLogger logger) {
    Dotenv dotenv = Dotenv.load();
    String openaiApiKey = Objects.requireNonNull(dotenv.get("OPENAI_API_KEY"));
    Utils.logPartialApiKey(logger, openaiApiKey);
    return openaiApiKey;
  }
}
