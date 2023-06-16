package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.processor.EmbeddingProcessor;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import ai.knowly.langtorch.store.vectordb.integration.VectorStore;
import ai.knowly.langtorch.store.vectordb.integration.schema.SimilaritySearchQuery;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeVectorStoreSpec;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.Match;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The PineconeVectorStore class is an implementation of the VectorStore interface, which provides
 * integration with the Pinecone service for storing and querying vectors.
 */
public class PineconeVectorStore implements VectorStore {
  // Constants
  private final EmbeddingProcessor embeddingProcessor;
  private final PineconeVectorStoreSpec pineconeVectorStoreSpec;

  @Inject
  public PineconeVectorStore(
      EmbeddingProcessor embeddingProcessor, PineconeVectorStoreSpec pineconeVectorStoreSpec) {
    this.embeddingProcessor = embeddingProcessor;
    this.pineconeVectorStoreSpec = pineconeVectorStoreSpec;
  }

  /** Adds the specified documents to the Pinecone vector store database.
   *
   * @return true if documents added successfully, otherwise false
   */
  @Override
  public boolean addDocuments(List<DomainDocument> documents) {
    if (documents.isEmpty()) {
      throw new IllegalStateException("Attempted to add an empty list");
    }

    return addVectors(
        documents.stream()
            .map(this::createVector)
            .collect(Collectors.toCollection(ArrayList::new)));
  }

  /** Adds a list of vectors to the Pinecone vector store database.
   *
   * @return true if vectors added successfully, otherwise false
   */
  private boolean addVectors(List<Vector> vectors) {
    UpsertRequest.UpsertRequestBuilder upsertRequestBuilder =
        UpsertRequest.builder().setVectors(vectors);
    this.pineconeVectorStoreSpec.getNamespace().ifPresent(upsertRequestBuilder::setNamespace);
    UpsertResponse response = this.pineconeVectorStoreSpec.getPineconeService().upsert(upsertRequestBuilder.build());
    return response.getUpsertedCount() == vectors.size();
  }

  /**
   * Creates an instance of Vector from given DomainDocument
   *
   * @param document the document from which a Vector will be created
   * @return an instance of {@link Vector}
   */
  private Vector createVector(DomainDocument document) {
    EmbeddingOutput embeddingOutput =
        embeddingProcessor.run(
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
   * schema documents and their corresponding similarity scores.
   */
  @Override
  public List<Pair<DomainDocument, Double>> similaritySearchVectorWithScore(
      SimilaritySearchQuery similaritySearchQuery) {

    QueryRequest.QueryRequestBuilder requestBuilder =
        QueryRequest.builder()
            .setIncludeMetadata(true)
            .setTopK(similaritySearchQuery.getTopK())
            .setVector(similaritySearchQuery.getQuery())
            .setFilter(similaritySearchQuery.getFilter());

    pineconeVectorStoreSpec.getNamespace().ifPresent(requestBuilder::setNamespace);
    QueryResponse response =
        pineconeVectorStoreSpec.getPineconeService().query(requestBuilder.build());

    List<Pair<DomainDocument, Double>> result = new ArrayList<>();

    // create mapping of PineCone metadata to schema meta data
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
