package ai.knowly.langtorch.llm.openai;

import static ai.knowly.langtorch.util.OpenAIServiceTestingUtils.OPENAI_TESTING_SERVICE;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.knowly.langtorch.llm.openai.schema.dto.embedding.Embedding;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingRequest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class EmbeddingTest {

  @Test
  void createEmbeddings() {
    OpenAIService service = OPENAI_TESTING_SERVICE;
    EmbeddingRequest embeddingRequest =
        EmbeddingRequest.builder()
            .model("text-similarity-babbage-001")
            .input(Collections.singletonList("The food was delicious and the waiter..."))
            .build();

    List<Embedding> embeddings = service.createEmbeddings(embeddingRequest).getData();

    assertFalse(embeddings.isEmpty());
    assertFalse(embeddings.get(0).getValue().isEmpty());
  }
}
