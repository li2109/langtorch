package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.*;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAiService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionChoice;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionChunk;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class CompletionTest {

  String token = Utils.getOpenAIApiKeyFromEnv();
  OpenAiService service = new OpenAiService(token);

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
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

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void streamCompletion() {
    CompletionRequest completionRequest =
        CompletionRequest.builder()
            .model("ada")
            .prompt("Somebody once told me the world is gonna roll me")
            .echo(true)
            .n(1)
            .maxTokens(25)
            .user("testing")
            .logitBias(new HashMap<>())
            .logprobs(5)
            .stream(true)
            .build();

    List<CompletionChunk> chunks = new ArrayList<>();
    service.streamCompletion(completionRequest).blockingForEach(chunks::add);
    assertTrue(chunks.size() > 0);
    assertNotNull(chunks.get(0).getChoices().get(0));
  }
}
