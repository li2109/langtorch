package ai.knowly.langtorch.llm.openai;

import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.knowly.langtorch.llm.openai.schema.dto.embedding.Embedding;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingRequest;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableOpenAILLMServiceLiveTrafficTest")
class EmbeddingTest {
  @Inject private OpenAIService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new OpenAIServiceConfigTestingModule())
        .injectMembers(this);
  }

  @Test
  void createEmbeddings() {
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
