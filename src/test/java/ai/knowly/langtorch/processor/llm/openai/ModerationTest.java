package ai.knowly.langtorch.processor.llm.openai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.knowly.langtorch.processor.llm.openai.service.OpenAIService;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.moderation.Moderation;
import ai.knowly.langtorch.processor.llm.openai.service.schema.dto.moderation.ModerationRequest;
import ai.knowly.langtorch.utils.ApiKeyUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.TestingUtils#testWithHttpRequestEnabled")
class ModerationTest {
  @Test
  void createModeration() {
    String token = ApiKeyUtils.getOpenAIApiKeyFromEnv();
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
