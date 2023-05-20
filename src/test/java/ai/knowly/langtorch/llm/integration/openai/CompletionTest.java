package ai.knowly.langtorch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.llm.Utils;
import ai.knowly.langtorch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.completion.CompletionChoice;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.completion.CompletionRequest;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class CompletionTest {

  @Test
  @EnabledIf("ai.knowly.langtorch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createCompletion() {
    String token = Utils.getOpenAIApiKeyFromEnv();
    OpenAIService service = new OpenAIService(token);
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
