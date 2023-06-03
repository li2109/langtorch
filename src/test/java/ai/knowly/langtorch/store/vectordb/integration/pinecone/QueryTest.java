package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.PineconeKeyUtil;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

@EnabledIf("ai.knowly.langtorch.util.TestingSettingUtils#enablePineconeVectorStoreLiveTrafficTest")
final class QueryTest {
  @Test
  void test() {
    // Arrange.
    String token = PineconeKeyUtil.getKey(Environment.TEST);
    PineconeService service =
        PineconeService.create(
            PineconeServiceConfig.builder()
                .setApiKey(token)
                .setEndpoint("https://test1-c4943a1.svc.us-west4-gcp-free.pinecone.io")
                .build());

    UpsertRequest upsertRequest =
        UpsertRequest.builder()
            .setVectors(
                Arrays.asList(
                    Vector.builder()
                        .setId("test2")
                        .setValues(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8))
                        .setMetadata(ImmutableMap.of("key", "val"))
                        .build()))
            .setNamespace("namespace")
            .build();

    QueryRequest queryRequest =
        QueryRequest.builder()
            .setVector(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8))
            .setTopK(3)
            .setNamespace("namespace")
            .setIncludeValues(true)
            .setIncludeMetadata(true)
            .build();

    // Act.
    UpsertResponse response = service.upsert(upsertRequest);
    QueryResponse queryResponse = service.query(queryRequest);

    // Assert.
    assertThat(response.getUpsertedCount()).isEqualTo(1);
    assertThat(queryResponse.getMatches()).isNotEmpty();
    assertThat(queryResponse.getMatches().get(0).getId()).isEqualTo("test2");
    assertThat(queryResponse.getMatches().get(0).getValues()).isNotEmpty();
    assertThat(queryResponse.getMatches().get(0).getMetadata()).isNotEmpty();
    assertThat(queryResponse.getNamespace()).isEqualTo("namespace");
  }
}
