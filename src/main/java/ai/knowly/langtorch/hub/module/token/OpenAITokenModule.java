package ai.knowly.langtorch.hub.module.token;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import java.util.concurrent.atomic.AtomicLong;

public class OpenAITokenModule extends AbstractModule {

  @Provides
  @Singleton
  public static TokenUsage provideTokenUsageContainer() {
    return TokenUsage.builder()
        .setPromptTokenUsage(new AtomicLong(0))
        .setCompletionTokenUsage(new AtomicLong(0))
        .build();
  }

  @Override
  protected void configure() {
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(EnableOpenAITokenRecord.class),
        new OpenAITokenUsageInterceptor(getProvider(TokenUsage.class)));
  }
}
