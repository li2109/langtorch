package ai.knowly.langtorch.llm.openai;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.hub.module.cache.LLMCacheModule;
import ai.knowly.langtorch.hub.module.token.OpenAITokenModule;
import ai.knowly.langtorch.hub.module.token.TokenUsage;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class CompletionTest {
  @Inject private OpenAIService service;
  @Inject private TokenUsage tokenUsage;

  @BeforeEach
  void setUp() {
    Guice.createInjector(
            BoundFieldModule.of(this),
            new OpenAIServiceConfigTestingModule(),
            new LLMCacheModule(),
            new OpenAITokenModule())
        .injectMembers(this);
  }

  @Test
  void createCompletion() {
    CompletionRequest completionRequest =
        CompletionRequest.builder()
            .model("ada")
            .prompt("Somebody once told me the world is gonna roll me")
            .echo(true)
            .n(5)
            .maxTokens(50)
            .user("testing")
            .logitBias(new HashMap<>())
            .logprobs(5)
            .build();

    CompletionResult result = service.createCompletion(completionRequest);

    assertThat(result.getUsage().getCompletionTokens())
        .isEqualTo(tokenUsage.getCompletionTokenUsage().get());
    assertThat(result.getUsage().getPromptTokens())
        .isEqualTo(tokenUsage.getPromptTokenUsage().get());
    assertThat(result.getChoices().size()).isEqualTo(5);
  }

  @Test
  void createCompletion_testCache() {
    // Arrange.
    CompletionRequest completionRequest =
        CompletionRequest.builder()
            .model("ada")
            .prompt("Somebody once told me the world is gonna roll me")
            .echo(true)
            .n(5)
            .maxTokens(50)
            .user("testing")
            .logitBias(new HashMap<>())
            .logprobs(5)
            .build();

    // Act.
    long startTime = System.nanoTime();
    CompletionResult result = service.createCompletion(completionRequest);
    long afterFirstStatementTime = System.nanoTime();
    AtomicLong tokenUsage1 = tokenUsage.getCompletionTokenUsage();
    System.out.println(
        "First completion time without cache in seconds: "
            + (afterFirstStatementTime - startTime) / 1000000000.0);

    CompletionResult result2 = service.createCompletion(completionRequest);
    long afterSecondStatementTime = System.nanoTime();
    AtomicLong tokenUsage2 = tokenUsage.getCompletionTokenUsage();
    System.out.println(
        "Second completion time with cache in seconds: "
            + (afterSecondStatementTime - afterFirstStatementTime) / 1000000000.0);

    // Act.
    assertThat(tokenUsage1.get()).isEqualTo(tokenUsage2.get());
    assertThat(afterSecondStatementTime - afterFirstStatementTime)
        .isLessThan(afterFirstStatementTime - startTime);
  }
}
