package ai.knowly.langtorch.processor.cohere.generate;

import ai.knowly.langtorch.llm.cohere.schema.CohereGenerateRequest;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


final class CohereGenerateRequestConverterTest {

    @Test
    void convert(){
        // Arrange.
        CohereGenerateProcessorConfig config = CohereGenerateProcessorConfig.builder()
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
                .build();
        String prompt = "apple";

        // Act.
        CohereGenerateRequest actualRequest = CohereGenerateRequestConverter.convert(prompt, config);

        // Assert.
        assertEquals("apple", actualRequest.prompt());
        assertEquals(1.0, actualRequest.p());
        assertEquals(4, actualRequest.k());
        assertEquals(4, actualRequest.maxTokens());
        assertEquals(4, actualRequest.maxTokens());
        assertIterableEquals(ImmutableList.of("ice"), actualRequest.endSequences());
        assertIterableEquals(ImmutableList.of("cream"), actualRequest.stopSequences());
        assertEquals("command", actualRequest.model());
        assertEquals(3, actualRequest.numGenerations());
        assertEquals(1.0, actualRequest.frequencyPenalty());
        assertEquals(2.0, actualRequest.presencePenalty());
        assertEquals("GENERATION", actualRequest.returnLikelihoods());
        assertEquals("START", actualRequest.truncate());
        assertEquals(5.0, actualRequest.temperature());
    }
}
