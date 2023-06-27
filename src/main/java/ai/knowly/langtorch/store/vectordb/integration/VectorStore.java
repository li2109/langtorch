package ai.knowly.langtorch.store.vectordb.integration;

import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.store.vectordb.integration.schema.SimilaritySearchQuery;
import java.util.List;

/** A shared interface for all Vector Store Databases */
public interface VectorStore {

    boolean addDocuments(List<DomainDocument> documents);
    List<DomainDocument> similaritySearch(SimilaritySearchQuery similaritySearchQuery);
    boolean updateDocuments(List<DomainDocument> documents);
    boolean deleteDocuments(List<DomainDocument> documents);
    boolean deleteDocumentsByIds(List<String> documentsIds);
}
