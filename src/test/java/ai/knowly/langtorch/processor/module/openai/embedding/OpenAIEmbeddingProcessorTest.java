package ai.knowly.langtorch.processor.module.openai.embedding;

import static ai.knowly.langtorch.schema.embeddings.EmbeddingType.OPEN_AI;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.openai.OpenAIService;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.Embedding;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import com.google.common.collect.ImmutableList;
import com.google.inject.testing.fieldbinder.Bind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class OpenAIEmbeddingProcessorTest {
  @Bind @Mock private OpenAIService openAIService;
  private OpenAIEmbeddingProcessor openAIEmbeddingProcessor;

  @BeforeEach
  public void setUp() {
    openAIEmbeddingProcessor =
        new OpenAIEmbeddingProcessor(
            openAIService, OpenAIEmbeddingsProcessorConfig.getDefaultInstance());
  }

  @Test
  void testRunWithChatProcessorConfig() {
    // Arrange
    EmbeddingResult embeddingResult = new EmbeddingResult();
    Embedding embedding = new Embedding();
    embedding.setIndex(1);
    embedding.setValue(ImmutableList.of(1.0, 2.0, 3.0));
    embeddingResult.setData(ImmutableList.of(embedding));
    embeddingResult.setModel("some-awesome-model");
    when(openAIService.createEmbeddings(any())).thenReturn(embeddingResult);

    // Act
    EmbeddingOutput output =
        openAIEmbeddingProcessor.run(
            EmbeddingInput.builder()
                .setModel("some-awesome-model")
                .setInput(ImmutableList.of("THIS IS A TEST"))
                .build());

    // Assert
    assertThat(output.getType()).isEqualTo(OPEN_AI);
    assertThat(output.getValue().get(0).getVector())
        .containsExactlyElementsIn(embedding.getValue());
  }
}
