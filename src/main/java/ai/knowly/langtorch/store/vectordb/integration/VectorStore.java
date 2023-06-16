package ai.knowly.langtorch.store.vectordb.integration;

import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.store.vectordb.integration.schema.SimilaritySearchQuery;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/** A shared interface for all Vector Store Databases */
public interface VectorStore {

    //TODO:: add updateDocuments and deleteDocuments methods

    boolean addDocuments(List<DomainDocument> documents);
    List<Pair<DomainDocument, Double>> similaritySearchVectorWithScore(SimilaritySearchQuery similaritySearchQuery);
}
