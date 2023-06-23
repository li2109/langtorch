package ai.knowly.langtorch.hub.module.token;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.concurrent.Executors;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;

public class OpenAITokenUsageInterceptor implements MethodInterceptor {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final Provider<TokenUsage> tokenUsage;

  @Inject
  public OpenAITokenUsageInterceptor(Provider<TokenUsage> tokenUsage) {
    this.tokenUsage = tokenUsage;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object result = invocation.proceed();

    boolean instanceOfListenableFuture = result instanceof ListenableFuture;

    if (!instanceOfListenableFuture) {
      return result;
    }

    // Create a new SettableFuture to return
    SettableFuture<Object> newFuture = SettableFuture.create();

    Futures.addCallback(
        (ListenableFuture<?>) result,
        new FutureCallback<Object>() {
          @Override
          public void onSuccess(Object result) {
            if (result instanceof ChatCompletionResult) {
              handleChatCompletionTokenCount((ChatCompletionResult) result);
            }
            if (result instanceof CompletionResult) {
              handleCompletionTokenCount((CompletionResult) result);
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

  private void handleCompletionTokenCount(CompletionResult result) {
    tokenUsage.get().getPromptTokenUsage().getAndAdd(result.getUsage().getPromptTokens());
    tokenUsage.get().getCompletionTokenUsage().addAndGet(result.getUsage().getCompletionTokens());
  }

  private void handleChatCompletionTokenCount(ChatCompletionResult result) {
    tokenUsage.get().getPromptTokenUsage().getAndAdd(result.getUsage().getPromptTokens());
    tokenUsage.get().getCompletionTokenUsage().addAndGet(result.getUsage().getCompletionTokens());
  }
}
