package ai.knowly.langtorch.llm.openai;

import static ai.knowly.langtorch.util.OpenAIServiceTestingUtils.OPENAI_TESTING_SERVICE;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.knowly.langtorch.llm.openai.schema.dto.moderation.Moderation;
import ai.knowly.langtorch.llm.openai.schema.dto.moderation.ModerationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class ModerationTest {
  @Test
  void createModeration() {
    OpenAIService service = OPENAI_TESTING_SERVICE;
    ModerationRequest moderationRequest =
        ModerationRequest.builder()
            .input("I want to kill them")
            .model("text-moderation-latest")
            .build();

    Moderation moderationScore = service.createModeration(moderationRequest).getResults().get(0);

    assertTrue(moderationScore.isFlagged());
  }
}
