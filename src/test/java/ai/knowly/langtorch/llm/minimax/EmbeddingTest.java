package ai.knowly.langtorch.llm.minimax;

import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.schema.embeddings.MiniMaxEmbeddingTypeScene;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableMiniMaxLLMServiceLiveTrafficTest")
class EmbeddingTest {

  @Inject private MiniMaxService service;

  @BeforeEach
  void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new MiniMaxServiceConfigTestingModule())
        .injectMembers(this);
  }

  @Test
  void createEmbeddings() {
    EmbeddingRequest embeddingRequest =
        EmbeddingRequest.builder()
            .model("embo-01")
            .texts(Collections.singletonList("The food was delicious and the waiter..."))
            .type(MiniMaxEmbeddingTypeScene.DB.toString())
            .build();

    EmbeddingResult embeddingResult = service.createEmbeddings(embeddingRequest);

    assertFalse(embeddingResult.getVectors().isEmpty());
  }
}
