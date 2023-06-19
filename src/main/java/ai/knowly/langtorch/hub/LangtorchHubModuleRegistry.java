package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.module.token.OpenAITokenModule;
import ai.knowly.langtorch.hub.schema.OpenAIKeyConfig;
import ai.knowly.langtorch.llm.openai.modules.key.OpenAIServiceConfigWithExplicitAPIKeyModule;
import ai.knowly.langtorch.llm.openai.modules.key.OpenAIServiceConfigWithImplicitAPIKeyModule;
import ai.knowly.langtorch.processor.openai.chat.OpenAIChatProcessorConfig;
import ai.knowly.langtorch.store.memory.conversation.ConversationMemory;
import com.google.inject.AbstractModule;
import java.util.ArrayList;
import java.util.List;

public final class LangtorchHubModuleRegistry extends AbstractModule {
  private final List<AbstractModule> modules;

  public static LangtorchHubModuleRegistry create() {
    // TODO: Pass in args here and process them.
    return new LangtorchHubModuleRegistry();
  }

  public List<AbstractModule> getModules() {
    return modules;
  }

  /**
   * Registers Open Ai related modules in langtorch hub.
   */
  public void registerOpenAiModule(OpenAIKeyConfig config) {
    modules.add(new OpenAITokenModule());
    modules.add(getOpenAIModule(config));
    modules.add(
        new AbstractModule() {
          @Override
          protected void configure() {
            bind(ConversationMemory.class).toInstance(ConversationMemory.getDefaultInstance());
            bind(OpenAIChatProcessorConfig.class)
                .toInstance(OpenAIChatProcessorConfig.getDefaultInstance());
          }
        });
  }

  private AbstractModule getOpenAIModule(OpenAIKeyConfig openAIKeyConfig) {
    if (openAIKeyConfig.isReadFromEnvFile()) {
      return new OpenAIServiceConfigWithImplicitAPIKeyModule();
    }
    if (!openAIKeyConfig.getOpenAiApiKey().isPresent()) {
      throw new IllegalArgumentException(
          "OpenAI API key is not present. Please provide the API key in the config.");
    } else {
      return new OpenAIServiceConfigWithExplicitAPIKeyModule(
          openAIKeyConfig.getOpenAiApiKey().get());
      }
  }

  private LangtorchHubModuleRegistry() {
    this.modules = new ArrayList<>();
  }
}
