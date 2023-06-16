package ai.knowly.langtorch.llm.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionChoice;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class CompletionTest {
  @Inject private OpenAIService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new OpenAIServiceConfigTestingModule())
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

    List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
    assertEquals(5, choices.size());
    assertNotNull(choices.get(0).getLogprobs());
  }
}
