package ai.knowly.langtorch.hub.module.token;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class OpenAITokenUsageInterceptor implements MethodInterceptor {
  private final Provider<TokenUsage> tokenUsage;

  @Inject
  public OpenAITokenUsageInterceptor(Provider<TokenUsage> tokenUsage) {
    this.tokenUsage = tokenUsage;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object result = invocation.proceed();

    if (result instanceof ListenableFuture) {
      // Create a new SettableFuture to return
      SettableFuture newFuture = SettableFuture.create();
      ListenableFuture originalFuture = (ListenableFuture) result;

      Futures.addCallback(
          originalFuture,
          new FutureCallback() {
            @Override
            public void onSuccess(Object result) {
              if (result instanceof ChatCompletionResult) {
                ChatCompletionResult chatCompletionResult = (ChatCompletionResult) result;
                tokenUsage
                    .get()
                    .getPromptTokenUsage()
                    .getAndAdd(chatCompletionResult.getUsage().getPromptTokens());
                tokenUsage
                    .get()
                    .getCompletionTokenUsage()
                    .addAndGet(chatCompletionResult.getUsage().getCompletionTokens());
              }
              if (result instanceof CompletionResult) {
                CompletionResult completionResult = (CompletionResult) result;
                tokenUsage
                    .get()
                    .getPromptTokenUsage()
                    .getAndAdd(completionResult.getUsage().getPromptTokens());
                tokenUsage
                    .get()
                    .getCompletionTokenUsage()
                    .addAndGet(completionResult.getUsage().getCompletionTokens());
              }
              newFuture.set(result);
            }

            public void onFailure(Throwable thrown) {
              // Add your own logic here
              System.out.println("Method failed");
              newFuture.setException(thrown);
            }
          },
          MoreExecutors.directExecutor());

      // Return newFuture instead of the original one
      return newFuture;
    }
    return result;
  }
}
