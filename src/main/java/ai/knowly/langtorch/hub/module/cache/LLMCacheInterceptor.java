package ai.knowly.langtorch.hub.module.cache;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.util.concurrent.Futures.immediateFuture;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionChoice;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.store.cache.Cache;
import ai.knowly.langtorch.store.cache.inmemory.exact.schema.TextCompletionCacheValue;
import ai.knowly.langtorch.store.cache.inmemory.exact.schema.TextCompletionCacheKey;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;

public class LLMCacheInterceptor implements MethodInterceptor {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final Provider<Cache<TextCompletionCacheKey, TextCompletionCacheValue>> cache;

  @Inject
  public LLMCacheInterceptor(
      Provider<Cache<TextCompletionCacheKey, TextCompletionCacheValue>> cache) {
    this.cache = cache;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    String strategy = invocation.getMethod().getAnnotation(EnableLLMCache.class).strategy();
    Object[] arguments = invocation.getArguments();
    if (arguments.length != 1 || !strategy.equals("EXACT_MATCH")) {
      return invocation.proceed();
    }

    Object result = invocation.proceed();
    boolean instanceOfListenableFuture = result instanceof ListenableFuture;
    if (!instanceOfListenableFuture) {
      return result;
    }

    Object input = arguments[0];
    if (!(input instanceof CompletionRequest)) {
      return result;
    }

    CompletionRequest completionRequest = (CompletionRequest) input;
    if (completionRequest.getTemperature() != null && completionRequest.getTemperature() > 0.2) {
      return result;
    }
    Optional<CompletionResult> completionResult = getFromCache(completionRequest);

    if (completionResult.isPresent()) {
      return immediateFuture(completionResult.get());
    }

    SettableFuture<Object> newFuture = SettableFuture.create();
    Futures.addCallback(
        (ListenableFuture<?>) result,
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(Object result) {
            if (result instanceof CompletionResult) {
              handleCompletionResult(completionRequest, (CompletionResult) result);
            }
            newFuture.set(result);
          }

          public void onFailure(@NotNull Throwable thrown) {
            logger.atWarning().withCause(thrown).log(
                "Failed to add callback in OpenAITokenUsageInterceptor");
            newFuture.setException(thrown);
          }
        },
        Executors.newCachedThreadPool());

    // Return newFuture instead of the original one
    return newFuture;
  }

  @NotNull
  private Optional<CompletionResult> getFromCache(CompletionRequest completionRequest) {
    return cache
        .get()
        .get(TextCompletionCacheKey.builder().setPrompt(completionRequest.getPrompt()).build())
        .map(
            value -> {
              CompletionResult completionResultFromCache = new CompletionResult();
              completionResultFromCache.setChoices(
                  value.getCompletion().stream()
                      .map(
                          completion -> {
                            CompletionChoice completionChoice = new CompletionChoice();
                            completionChoice.setText(completion);
                            return completionChoice;
                          })
                      .collect(Collectors.toList()));
              return completionResultFromCache;
            });
  }

  private void handleCompletionResult(CompletionRequest input, CompletionResult result) {
    cache
        .get()
        .put(
            TextCompletionCacheKey.builder().setPrompt(input.getPrompt()).build(),
            TextCompletionCacheValue.builder()
                .setCompletion(
                    result.getChoices().stream()
                        .map(CompletionChoice::getText)
                        .collect(toImmutableList()))
                .build());
  }
}
