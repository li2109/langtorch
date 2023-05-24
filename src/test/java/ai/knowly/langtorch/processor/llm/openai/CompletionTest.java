package ai.knowly.langtorch.processor.llm.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtorch.processor.llm.openai.service.OpenAIService;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.completion.CompletionChoice;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.utils.ApiKeyUtils;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.TestingUtils#testWithHttpRequestEnabled")
class CompletionTest {

  @Test
  void createCompletion() {
    String token = ApiKeyUtils.getOpenAIApiKeyFromEnv();
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
