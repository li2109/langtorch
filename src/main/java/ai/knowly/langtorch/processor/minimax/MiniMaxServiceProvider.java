package ai.knowly.langtorch.processor.module.minimax;

import ai.knowly.langtorch.llm.minimax.MiniMaxApi;
import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.MiniMaxKeyUtil;
import com.google.common.flogger.FluentLogger;
import java.time.Duration;

/**
 * @author maxiao
 * @date 2023/06/07
 */
public final class MiniMaxServiceProvider {
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private MiniMaxServiceProvider() {}

  public static MiniMaxApi createMiniMaxAPI(String groupId, String apiKey) {
    return MiniMaxService.buildApi(groupId, apiKey, DEFAULT_TIMEOUT);
  }

  public static MiniMaxApi createMiniMaxAPI() {
    return MiniMaxService.buildApi(
        MiniMaxKeyUtil.getGroupId(logger, Environment.PRODUCTION),
        MiniMaxKeyUtil.getKey(logger, Environment.PRODUCTION),
        DEFAULT_TIMEOUT);
  }

  public static MiniMaxService createMiniMaxService(String groupId, String apiKey) {
    return MiniMaxService.create(createMiniMaxAPI(groupId, apiKey));
  }

  public static MiniMaxService createMiniMaxService() {
    return MiniMaxService.create(createMiniMaxAPI());
  }
}
