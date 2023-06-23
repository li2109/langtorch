package ai.knowly.langtorch.hub.module.cache;

import ai.knowly.langtorch.store.cache.Cache;
import ai.knowly.langtorch.store.cache.inmemory.exact.InMemoryExactMatchCache;
import ai.knowly.langtorch.store.cache.inmemory.exact.InMemoryExactMatchCacheSpec;
import ai.knowly.langtorch.store.cache.inmemory.exact.schema.TextCompletionCacheValue;
import ai.knowly.langtorch.store.cache.inmemory.exact.schema.TextCompletionCacheKey;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import org.jetbrains.annotations.NotNull;

public class LLMCacheModule extends AbstractModule {
  @Provides
  @Singleton
  public static Cache<TextCompletionCacheKey, TextCompletionCacheValue> provideExactMatchCatch() {
    return new InMemoryExactMatchCache<>(InMemoryExactMatchCacheSpec.getDefaultInstance());
  }

  @Override
  protected void configure() {
    bindInterceptor(
        Matchers.any(),
        Matchers.annotatedWith(EnableLLMCache.class),
        new LLMCacheInterceptor(getProvider(Key.get(getCacheTypeLiteral()))));
  }

  @NotNull
  private static TypeLiteral<Cache<TextCompletionCacheKey, TextCompletionCacheValue>>
      getCacheTypeLiteral() {
    return new TypeLiteral<Cache<TextCompletionCacheKey, TextCompletionCacheValue>>() {};
  }
}
