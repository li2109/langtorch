package ai.knowly.langtorch.llm.minimax;

import static ai.knowly.langtorch.util.MiniMaxServiceTestingUtils.MINIMAX_TESTING_SERVICE;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enableMiniMaxLLMServiceLiveTrafficTest")
class EmbeddingTest {

  @Test
  void createEmbeddings() {
    MiniMaxService service = MINIMAX_TESTING_SERVICE;
    EmbeddingRequest embeddingRequest =
        EmbeddingRequest.builder()
            .model("embo-01")
            .texts(Collections.singletonList("The food was delicious and the waiter..."))
            .type("db")
            .build();

    EmbeddingResult embeddingResult = service.createEmbeddings(embeddingRequest);

    assertFalse(embeddingResult.getVectors().isEmpty());
  }
}
