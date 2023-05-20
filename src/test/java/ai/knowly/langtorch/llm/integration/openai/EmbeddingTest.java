package ai.knowly.langtorch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.knowly.langtorch.llm.Utils;
import ai.knowly.langtorch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.embedding.Embedding;
import ai.knowly.langtorch.llm.integration.openai.service.schema.dto.embedding.EmbeddingRequest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class EmbeddingTest {

  @Test
  @EnabledIf("ai.knowly.langtorch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
  void createEmbeddings() {
    String token = Utils.getOpenAIApiKeyFromEnv();
    OpenAIService service = new OpenAIService(token);
    EmbeddingRequest embeddingRequest =
        EmbeddingRequest.builder()
            .model("text-similarity-babbage-001")
            .input(Collections.singletonList("The food was delicious and the waiter..."))
            .build();

    List<Embedding> embeddings = service.createEmbeddings(embeddingRequest).getData();

    assertFalse(embeddings.isEmpty());
    assertFalse(embeddings.get(0).getEmbedding().isEmpty());
  }
}
