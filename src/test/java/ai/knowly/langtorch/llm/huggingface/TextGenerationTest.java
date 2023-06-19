package ai.knowly.langtorch.llm.huggingface;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskRequest;
import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskResponse;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf(
    "ai.knowly.langtorch.util.TestingSettingUtils#enableHuggingFaceLLMServiceLiveTrafficTest")
class TextGenerationTest {
  @Inject HuggingFaceService huggingFaceService;

  @BeforeEach
  void setUp() {
    Guice.createInjector(
            new HuggingFaceTestingServiceConfigModule("gpt2"), BoundFieldModule.of(this))
        .injectMembers(this);
  }

  @Test
  void test() {
    // Arrange.
    CreateTextGenerationTaskRequest createTextGenerationTaskRequest =
        CreateTextGenerationTaskRequest.builder()
            .setInputs("The answer to the universe is")
            .build();

    // Act.
    List<CreateTextGenerationTaskResponse> generatedText =
        huggingFaceService.createTextGenerationTask(createTextGenerationTaskRequest);

    // Assert.
    assertThat(generatedText.get(0)).isNotNull();
  }
}
