package ai.knowly.langtorch.llm.cohere;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateRequest;
import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateResponse;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableCohereLLMServiceLiveTrafficTest")
class GenerateTest {
  @Inject private CohereService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new CohereServiceConfigTestingModule())
        .injectMembers(this);
  }

  @Test
  void testGenerate() {
    // Arrange & Act.
    CohereGenerateResponse result =
        service.generate(
            CohereGenerateRequest.builder()
                .prompt(
                    "Describe the steps you would take to make a peanut butter and jelly sandwich.")
                .numGenerations(2)
                .build());

    // Assert.
    assertThat(result.getGenerations().size()).isEqualTo(2);
  }
}
