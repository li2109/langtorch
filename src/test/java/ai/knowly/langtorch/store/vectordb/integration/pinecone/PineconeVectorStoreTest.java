package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyList;

import ai.knowly.langtorch.processor.EmbeddingsProcessor;
import ai.knowly.langtorch.schema.embeddings.Embedding;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingType;
import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeSimilaritySearchQuery;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeVectorStoreSpec;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.SparseValues;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.Match;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import com.google.common.collect.ImmutableMap;
import java.util.*;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class PineconeVectorStoreTest {

  private String textKey;

  private PineconeVectorStore pineconeVectorStore;

  private PineconeService pineconeService;

  private EmbeddingsProcessor embeddingsProcessor;

  @BeforeEach
  void setUp() {
    textKey = "textKey";
    pineconeService = Mockito.mock(PineconeService.class);
    embeddingsProcessor = Mockito.mock(EmbeddingsProcessor.class);

    pineconeVectorStore =
        PineconeVectorStore.of(
            embeddingsProcessor,
            PineconeVectorStoreSpec.builder()
                .setPineconeService(pineconeService)
                .setTextKey(textKey)
                .build());
  }

  @Test
  void testAddDocuments() {
    EmbeddingOutput embeddingOutput = EmbeddingOutput.of(EmbeddingType.OPEN_AI, getEmbeddings());
    Mockito.when(embeddingsProcessor.run(ArgumentMatchers.any())).thenReturn(embeddingOutput);
    UpsertResponse upsertResponse = new UpsertResponse(1);
    Mockito.when(pineconeService.upsert(ArgumentMatchers.any())).thenReturn(upsertResponse);

    // Act.
    UpsertResponse response = pineconeVectorStore.addDocuments(getDocuments());
    // Assert.
    assertThat(response).isNotNull();
    assertThat(response.getUpsertedCount()).isEqualTo(1);
  }

  @Test
  void testAddDocumentsEmpty() {
    // Act.
    UpsertResponse response = pineconeVectorStore.addDocuments(emptyList());
    // Assert.
    assertThat(response).isNull();
  }

  @Test
  void testSimilaritySearchVectorWithScore() {
    double score = 0.5;
    String content = "Content";

    List<Match> matches = new ArrayList<>();
    Map<String, String> map = new HashMap<>();
    map.put(textKey, content);
    Match match =
        new Match(
            UUID.randomUUID().toString(), score, emptyList(), SparseValues.builder().build(), map);
    matches.add(match);

    QueryResponse queryResponse = new QueryResponse(matches, "");
    Mockito.when(pineconeService.query(ArgumentMatchers.any())).thenReturn(queryResponse);

    // Act.
    List<Pair<DomainDocument, Double>> result =
        pineconeVectorStore.similaritySearchVectorWithScore(
            PineconeSimilaritySearchQuery.builder().setQuery(emptyList()).setK(0L).build());
    // Assert.
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getLeft().getPageContent()).isEqualTo(content);
    assertThat(result.get(0).getRight()).isEqualTo(score);
  }

  @Test
  void testSimilaritySearchVectorWithScoreEmpty() {
    QueryResponse queryResponse = new QueryResponse(null, null);
    Mockito.when(pineconeService.query(ArgumentMatchers.any())).thenReturn(queryResponse);
    // Act.
    List<Pair<DomainDocument, Double>> result =
        pineconeVectorStore.similaritySearchVectorWithScore(
            PineconeSimilaritySearchQuery.builder().setQuery(emptyList()).setK(0L).build());
    // Assert.
    assertThat(result).isEmpty();
  }

  private List<DomainDocument> getDocuments() {
    ArrayList<DomainDocument> documents = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      DomainDocument document =
          DomainDocument.builder()
              .setId(UUID.randomUUID().toString())
              .setPageContent("content" + i)
              .setMetadata(Metadata.builder().setValue(ImmutableMap.of("key", "val")).build())
              .build();
      documents.add(document);
    }
    return documents;
  }

  private List<Embedding> getEmbeddings() {
    ArrayList<Embedding> embeddings = new ArrayList<>();
    embeddings.add(Embedding.of(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8)));
    return embeddings;
  }
}
