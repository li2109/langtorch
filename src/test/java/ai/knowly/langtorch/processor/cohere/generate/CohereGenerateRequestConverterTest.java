package ai.knowly.langtorch.processor.cohere.generate;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateRequest;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

final class CohereGenerateRequestConverterTest {

  @Test
  void convert() {
    // Arrange.
    CohereGenerateProcessorConfig config =
        CohereGenerateProcessorConfig.builder()
            .setP(1.0)
            .setK(4)
            .setMaxTokens(4)
            .setEndSequences(ImmutableList.of("ice"))
            .setStopSequences(ImmutableList.of("cream"))
            .setModel("command")
            .setNumGenerations(3)
            .setFrequencyPenalty(1.0)
            .setPresencePenalty(2.0)
            .setTruncate(CohereGenerateTruncate.START)
            .setReturnLikelihoods(CohereGenerateReturnLikelihoods.GENERATION)
            .setTemperature(5.0)
            .setPresent("present")
            .build();
    String prompt = "apple";

    // Act.
    CohereGenerateRequest actualRequest = CohereGenerateRequestConverter.convert(prompt, config);

    // Assert.
    assertThat(actualRequest.prompt()).isEqualTo("apple");
    assertThat(actualRequest.p()).isEqualTo(1.0);
    assertThat(actualRequest.k()).isEqualTo(4);
    assertThat(actualRequest.maxTokens()).isEqualTo(4);
    assertThat(actualRequest.maxTokens()).isEqualTo(4);
    assertThat(actualRequest.endSequences()).containsExactly("ice");
    assertThat(actualRequest.stopSequences()).containsExactly("cream");
    assertThat(actualRequest.model()).isEqualTo("command");
    assertThat(actualRequest.numGenerations()).isEqualTo(3);
    assertThat(actualRequest.frequencyPenalty()).isEqualTo(1.0);
    assertThat(actualRequest.presencePenalty()).isEqualTo(2.0);
    assertThat(actualRequest.returnLikelihoods()).isEqualTo("GENERATION");
    assertThat(actualRequest.truncate()).isEqualTo("START");
    assertThat(actualRequest.temperature()).isEqualTo(5.0);
    assertThat(actualRequest.preset()).isEqualTo("present");
  }
}
