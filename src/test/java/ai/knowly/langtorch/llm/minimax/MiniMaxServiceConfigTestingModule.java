package ai.knowly.langtorch.llm.minimax;

import ai.knowly.langtorch.llm.minimax.schema.config.MiniMaxServiceConfig;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.MiniMaxKeyUtil;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author maxiao
 * @date 2023/06/17
 */
public class MiniMaxServiceConfigTestingModule extends AbstractModule {
  // Get the MiniMax groupId and apiKey from the environment variables and provide it to the MiniMax
  // service.
  @Provides
  public MiniMaxServiceConfig provideMiniMaxServiceConfig() {
    return MiniMaxServiceConfig.builder()
        .setGroupId(MiniMaxKeyUtil.getGroupId(Environment.TEST))
        .setApiKey(MiniMaxKeyUtil.getKey(Environment.TEST))
        .build();
  }
}
