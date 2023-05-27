package ai.knowly.langtorch.processor.llm.openai;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.knowly.langtorch.processor.llm.openai.service.OpenAIService;
import ai.knowly.langtorch.processor.module.openai.embeddings.OpenAIEmbeddingsProcessor;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.Embeddings;
import ai.knowly.langtorch.util.OpenAIServiceTestingUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class OpenAIEmbeddingTest {

  @Mock private OpenAIService openAIService;
  private OpenAIEmbeddingsProcessor openAIEmbeddingsProcessor;

  @BeforeEach
  void setUp() {
    openAIEmbeddingsProcessor = new OpenAIEmbeddingsProcessor(openAIService);
  }

  @Test
  void testOpenAiEmbeddings() {

    String model = "model";
    List<String> inputData = new ArrayList<>();
    String user = "user";

    EmbeddingInput input = new EmbeddingInput(model, inputData, user);

    when(openAIService.createEmbeddings(any()))
        .thenReturn(OpenAIServiceTestingUtils.Embeddings.createEmbeddingResult());

    Embeddings result = openAIEmbeddingsProcessor.run(input);

    Assertions.assertEquals("OPEN_AI", result.getType().name());
    Assertions.assertNotNull(result);
  }
}
