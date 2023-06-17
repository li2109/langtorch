package ai.knowly.langtorch.llm.openai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.knowly.langtorch.llm.openai.schema.dto.moderation.Moderation;
import ai.knowly.langtorch.llm.openai.schema.dto.moderation.ModerationRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class ModerationTest {
  @Inject private OpenAIService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new OpenAIServiceConfigTestingModule())
        .injectMembers(this);
  }

  @Test
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
