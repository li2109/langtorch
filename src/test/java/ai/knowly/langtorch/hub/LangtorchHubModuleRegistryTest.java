package ai.knowly.langtorch.hub;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.hub.module.token.OpenAITokenModule;
import ai.knowly.langtorch.hub.schema.OpenAIKeyConfig;
import ai.knowly.langtorch.llm.openai.modules.key.OpenAIServiceConfigWithExplicitAPIKeyModule;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class LangtorchHubModuleRegistryTest {
  private static final String OPEN_AI_KEY = "OPEN_AI_KEY";

  @Test
  void create() {
    assertThat(LangtorchHubModuleRegistry.create()).isNotNull();
  }

  @Test
  void registerOpenAiModule_fromApiKey() {
    LangtorchHubModuleRegistry registry = LangtorchHubModuleRegistry.create();
    registry.registerOpenAiModule(OpenAIKeyConfig.createOpenConfigWithApiKey(OPEN_AI_KEY));

    assertThat(registry.getModules().stream().map(Object::getClass).collect(Collectors.toList()))
        .containsAtLeast(
            OpenAITokenModule.class, OpenAIServiceConfigWithExplicitAPIKeyModule.class);
  }
}
