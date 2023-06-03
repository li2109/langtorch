package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.processor.module.EmbeddingsProcessor;
import ai.knowly.langtorch.processor.module.openai.embeddings.OpenAIEmbeddingsProcessor;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import ai.knowly.langtorch.store.vectordb.integration.EmbeddingProcessorType;
import ai.knowly.langtorch.store.vectordb.integration.EmbeddingProcessorTypeNotFound;
import ai.knowly.langtorch.store.vectordb.integration.VectorStore;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeSimilaritySearchQuery;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeVectorStoreSpec;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.Match;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The PineconeVectorStore class is an implementation of the VectorStore interface, which provides
 * integration with the Pinecone service for storing and querying vectors.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class PineconeVectorStore implements VectorStore {

  // Constants
  private static final String DEFAULT_MODEL = "text-embedding-ada-002";
  @NonNull private final EmbeddingsProcessor embeddingsProcessor;
  private final PineconeVectorStoreSpec pineconeVectorStoreSpec;

  /** Creates a new instance of PineconeVectorStore based on embedding processor type. */
  public static PineconeVectorStore of(
      EmbeddingProcessorType embeddingProcessorType,
      PineconeVectorStoreSpec pineconeVectorStoreSpec) {
    Optional<EmbeddingsProcessor> processor;
    if (embeddingProcessorType == EmbeddingProcessorType.OPENAI) {
      processor = Optional.of(OpenAIEmbeddingsProcessor.create());
    } else {
      throw new EmbeddingProcessorTypeNotFound(
          String.format(
              "Embedding processor type %s is not supported.", embeddingProcessorType.name()));
    }

    return new PineconeVectorStore(processor.get(), pineconeVectorStoreSpec);
  }

  /** Creates a new instance of PineconeVectorStore with specified embedding processor and spec . */
  public static PineconeVectorStore of(
      EmbeddingsProcessor embeddingsProcessor, PineconeVectorStoreSpec pineconeVectorStoreSpec) {
    return new PineconeVectorStore(embeddingsProcessor, pineconeVectorStoreSpec);
  }

  /** Adds the specified documents to the Pinecone vector store database. */
  public UpsertResponse addDocuments(List<DomainDocument> documents) {
    if (documents.isEmpty()) {
      return null;
    }

    return addVectors(
        documents.stream()
            .map(this::createVector)
            .collect(Collectors.toCollection(ArrayList::new)));
  }

  /** Adds a list of vectors to the Pinecone vector store database. */
  private UpsertResponse addVectors(List<Vector> vectors) {
    UpsertRequest.UpsertRequestBuilder upsertRequestBuilder =
        UpsertRequest.builder().setVectors(vectors);
    this.pineconeVectorStoreSpec.getNamespace().ifPresent(upsertRequestBuilder::setNamespace);
    return this.pineconeVectorStoreSpec.getPineconeService().upsert(upsertRequestBuilder.build());
  }

  /**
   * Creates an instance of Vector from given DomainDocument
   *
   * @param document the document from which a Vector will be created
   * @return an instance of {@link Vector}
   */
  private Vector createVector(DomainDocument document) {
    EmbeddingOutput embeddingOutput =
        embeddingsProcessor.run(
            EmbeddingInput.builder()
                .setModel(pineconeVectorStoreSpec.getModel())
                .setInput(Collections.singletonList(document.getPageContent()))
                .build());
    return Vector.builder()
        .setId(document.getId().orElse(UUID.randomUUID().toString()))
        .setMetadata(document.getMetadata().orElse(Metadata.getDefaultInstance()).getValue())
        .setValues(embeddingOutput.getValue().get(0).getVector())
        .build();
  }

  /**
   * Performs a similarity search using a vector query and returns a list of pairs containing the
   * domain documents and their corresponding similarity scores.
   */
  public List<Pair<DomainDocument, Double>> similaritySearchVectorWithScore(
      PineconeSimilaritySearchQuery pineconeSimilaritySearchQuery) {

    QueryRequest.QueryRequestBuilder requestBuilder =
        QueryRequest.builder()
            .setIncludeMetadata(true)
            .setTopK(pineconeSimilaritySearchQuery.getK())
            .setVector(pineconeSimilaritySearchQuery.getQuery())
            .setFilter(pineconeSimilaritySearchQuery.getFilter());

    pineconeVectorStoreSpec.getNamespace().ifPresent(requestBuilder::setNamespace);
    QueryResponse response =
        pineconeVectorStoreSpec.getPineconeService().query(requestBuilder.build());

    List<Pair<DomainDocument, Double>> result = new ArrayList<>();

    // create mapping of PineCone metadata to domain meta data
    if (response.getMatches() != null) {
      for (Match match : response.getMatches()) {
        if (!pineconeVectorStoreSpec.getTextKey().isPresent()) {
          continue;
        }
        Metadata metadata =
            match.getMetadata() == null
                ? Metadata.getDefaultInstance()
                : Metadata.builder().setValue(match.getMetadata()).build();

        if (match.getScore() != null) {
          result.add(
              Pair.of(
                  DomainDocument.builder()
                      .setPageContent(
                          metadata.getValue().get(this.pineconeVectorStoreSpec.getTextKey().get()))
                      .setMetadata(metadata)
                      .build(),
                  match.getScore()));
        }
      }
    }

    return result;
  }
}
