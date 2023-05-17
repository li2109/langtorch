package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAiService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.moderation.Moderation;
import ai.knowly.langtoch.llm.integration.openai.service.schema.moderation.ModerationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class ModerationTest {

  String token = Utils.getOpenAIApiKeyFromEnv();
  OpenAiService service = new OpenAiService(token);

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createModeration() {
    ModerationRequest moderationRequest =
        ModerationRequest.builder()
            .input("I want to kill them")
            .model("text-moderation-latest")
            .build();

    Moderation moderationScore = service.createModeration(moderationRequest).getResults().get(0);

    assertTrue(moderationScore.isFlagged());
  }
}
