package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import ai.knowly.langtorch.utils.ApiKeyUtils;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.TestingUtils#testWithHttpRequestEnabled")
class FetchTest {
  @Test
  void test() {
    // Arrange.
    String token = ApiKeyUtils.getPineconeKeyFromEnv();
    PineconeService service =
        new PineconeService(
            PineconeServiceConfig.builder()
                .setApiKey(token)
                .setEndpoint("https://test1-c4943a1.svc.us-west4-gcp-free.pinecone.io")
                .build());

    Vector vector =
        Vector.builder()
            .setId("test2")
            .setValues(List.of(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8))
            .setMetadata(Map.of("key", "val"))
            .build();

    UpsertRequest upsertRequest =
        UpsertRequest.builder().setVectors(List.of(vector)).setNamespace("namespace").build();

    FetchRequest fetchRequest =
        FetchRequest.builder().setIds(List.of("test2")).setNamespace("namespace").build();

    // Act.
    UpsertResponse response = service.upsert(upsertRequest);
    FetchResponse fetchResponse = service.fetch(fetchRequest);

    // Assert.
    assertThat(response.getUpsertedCount()).isEqualTo(1);
    assertThat(fetchResponse.getVectors().get("test2")).isEqualTo(vector);
  }
}
