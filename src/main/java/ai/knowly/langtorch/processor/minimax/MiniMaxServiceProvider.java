package ai.knowly.langtorch.processor.minimax;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.config.MiniMaxServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.MiniMaxKeyUtil;
import com.google.common.flogger.FluentLogger;

/**
 * @author maxiao
 * @date 2023/06/07
 */
public final class MiniMaxServiceProvider {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private MiniMaxServiceProvider() {}

  public static MiniMaxService createMiniMaxService(String groupId, String apiKey) {

    return new MiniMaxService(
        MiniMaxServiceConfig.builder().setGroupId(groupId).setApiKey(apiKey).build());
  }

  public static MiniMaxService createMiniMaxService() {

    return createMiniMaxService(
        MiniMaxKeyUtil.getGroupId(logger, Environment.PRODUCTION),
        MiniMaxKeyUtil.getKey(logger, Environment.PRODUCTION));
  }
}
