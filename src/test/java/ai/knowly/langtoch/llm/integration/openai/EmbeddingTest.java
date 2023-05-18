package ai.knowly.langtoch.llm.integration.openai;

import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.knowly.langtoch.llm.Utils;
import ai.knowly.langtoch.llm.integration.openai.service.OpenAIService;
import ai.knowly.langtoch.llm.integration.openai.service.schema.embedding.Embedding;
import ai.knowly.langtoch.llm.integration.openai.service.schema.embedding.EmbeddingRequest;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

class EmbeddingTest {

  @Test
  @EnabledIf("ai.knowly.langtoch.llm.integration.openai.TestingUtils#testWithHttpRequestEnabled")
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
