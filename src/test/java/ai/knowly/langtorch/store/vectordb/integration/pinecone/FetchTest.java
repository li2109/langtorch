package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import static ai.knowly.langtorch.util.PineconeTestingUtils.PINECONE_TESTING_SERVICE;
import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enablePineconeVectorStoreLiveTrafficTest")
class FetchTest {
  @Test
  void test() {
    // Arrange.
    PineconeService service = PINECONE_TESTING_SERVICE;

    Vector vector =
        Vector.builder()
            .setId("test2")
            .setValues(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8))
            .setMetadata(ImmutableMap.of("key", "val"))
            .build();

    UpsertRequest upsertRequest =
        UpsertRequest.builder().setVectors(Arrays.asList(vector)).setNamespace("namespace").build();

    FetchRequest fetchRequest =
        FetchRequest.builder().setIds(Arrays.asList("test2")).setNamespace("namespace").build();

    // Act.
    UpsertResponse response = service.upsert(upsertRequest);
    FetchResponse fetchResponse = service.fetch(fetchRequest);

    // Assert.
    assertThat(response.getUpsertedCount()).isEqualTo(1);
    assertThat(fetchResponse.getVectors().get("test2")).isEqualTo(vector);
  }
}
