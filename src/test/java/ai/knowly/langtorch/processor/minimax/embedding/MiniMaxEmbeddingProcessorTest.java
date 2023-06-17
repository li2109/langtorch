package ai.knowly.langtorch.processor.minimax.embedding;

import static ai.knowly.langtorch.schema.embeddings.EmbeddingType.MINI_MAX;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.llm.minimax.MiniMaxService;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.processor.minimax.embeddings.MiniMaxEmbeddingsProcessorConfig;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import com.google.common.collect.ImmutableList;
import com.google.inject.testing.fieldbinder.Bind;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author maxiao
 * @date 2023/06/17
 */
@ExtendWith(MockitoExtension.class)
public class MiniMaxEmbeddingProcessorTest {

  @Bind @Mock private MiniMaxService miniMaxService;
  private ai.knowly.langtorch.processor.minimax.embeddings.MiniMaxEmbeddingsProcessor
      miniMaxChatProcessor;

  @BeforeEach
  public void setUp() {
    miniMaxChatProcessor =
        new ai.knowly.langtorch.processor.minimax.embeddings.MiniMaxEmbeddingsProcessor(
            miniMaxService, MiniMaxEmbeddingsProcessorConfig.getDefaultInstance());
  }

  @Test
  void testRunWithChatProcessorConfig() {
    // Arrange
    EmbeddingResult embeddingResult = new EmbeddingResult();
    List<List<Float>> vectors =
        ImmutableList.of(ImmutableList.of(1.0f, 2.0f, 3.0f), ImmutableList.of(1.0f, 2.0f, 3.0f));
    embeddingResult.setVectors(vectors);
    when(miniMaxService.createEmbeddings(any())).thenReturn(embeddingResult);

    // Act
    EmbeddingOutput output =
        miniMaxChatProcessor.run(
            EmbeddingInput.builder()
                .setModel("embo-01")
                .setInput(ImmutableList.of("THIS IS A TEST"))
                .build());

    // Assert
    assertThat(output.getType()).isEqualTo(MINI_MAX);
    assertThat(output.getValue().get(0).getFloatVector()).containsExactlyElementsIn(vectors.get(0));
  }
}
