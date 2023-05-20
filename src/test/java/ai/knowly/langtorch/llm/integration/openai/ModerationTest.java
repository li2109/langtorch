package ai.knowly.langtorch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.knowly.langtorch.llm.Utils;
import ai.knowly.langtorch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.moderation.Moderation;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.moderation.ModerationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class ModerationTest {
  @Test
  @EnabledIf("ai.knowly.langtorch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createModeration() {
    String token = Utils.getOpenAIApiKeyFromEnv();
    OpenAIService service = new OpenAIService(token);
    ModerationRequest moderationRequest =
        ModerationRequest.builder()
            .input("I want to kill them")
            .model("text-moderation-latest")
            .build();

    Moderation moderationScore = service.createModeration(moderationRequest).getResults().get(0);

    assertTrue(moderationScore.isFlagged());
  }
}
