package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.processor.module.EmbeddingsProcessor;
import ai.knowly.langtorch.processor.module.openai.embeddings.OpenAIEmbeddingsProcessor;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import ai.knowly.langtorch.store.vectordb.integration.VectorStore;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.Match;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import ai.knowly.langtorch.utils.ApiEndPointUtils;
import ai.knowly.langtorch.utils.ApiKeyUtils;
import com.google.common.flogger.FluentLogger;
import kotlin.Pair;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The PineconeVectorStore class is an implementation of the VectorStore interface, which provides integration with
 * the Pinecone service for storing and querying vectors.
 */
public class PineconeVectorStore implements VectorStore {

    // Constants
    private static final String DEFAULT_MODEL = "text-embedding-ada-002";

    // Instance variables
    private final EmbeddingsProcessor embeddingsProcessor;
    private final PineconeService pineconeService;
    private final Optional<String> namespace;
    private final Optional<String> textKey;
    Map<String, String> filter;
    private final Optional<String> model;

    /**
     * Private constructor used by the static factory methods to create a new instance of PineconeVectorStore.
     *
     * @param embeddingsProcessor The embeddings output processor.
     * @param service   The Pinecone service.
     * @param namespace The optional namespace for the Pinecone service.
     * @param textKey   The optional text key for the Pinecone service.
     * @param model     The optional model for embeddings.
     */
    private PineconeVectorStore(
            EmbeddingsProcessor embeddingsProcessor,
            PineconeService service,
            Optional<String> namespace,
            Optional<String> textKey,
            Optional<String> model
    ) {
        this.embeddingsProcessor = embeddingsProcessor;
        this.pineconeService = service;
        this.namespace = namespace;
        this.textKey = textKey;
        this.model = model;
    }

    /**
     * Creates a new instance of PineconeVectorStore with OpenAI embeddings.
     *
     * @param pineconeService The PineconeService for Pinecone API calls.
     * @param namespace The optional namespace for the Pinecone service.
     * @param textKey   The optional text key for the Pinecone service.
     * @param model     The optional LLM model for embeddings.
     * @return A new instance of PineconeVectorStore.
     */
    public static PineconeVectorStore ofOpenAI(
            PineconeService pineconeService,
            Optional<String> namespace,
            Optional<String> textKey,
            Optional<String> model
    ) {
        return create(
                OpenAIEmbeddingsProcessor.create(),
                pineconeService,
                namespace,
                textKey,
                model
        );
    }


    /**
     * Creates a new instance of PineconeVectorStore with OpenAI embeddings.
     *
     * @param output    The LLM Processor for embeddings
     * @param pineconeService The PineconeService for Pinecone API calls.
     * @param namespace The optional namespace for the Pinecone service.
     * @param textKey   The optional text key for the Pinecone service.
     * @param model     The optional LLM model for embeddings.
     * @return A new instance of PineconeVectorStore.
     */
    public static PineconeVectorStore create(
            EmbeddingsProcessor output,
            PineconeService pineconeService,
            Optional<String> namespace,
            Optional<String> textKey,
            Optional<String> model
    ) {
        return new PineconeVectorStore(output, pineconeService, namespace, textKey, model);
    }

    /**
     * Creates a new instance of PineconeVectorStore with OpenAI embeddings using default configuration values.
     *
     * @param logger The FluentLogger for logging.
     * @param model  The optional model for embeddings.
     * @return A new instance of PineconeVectorStore.
     */
    public static PineconeVectorStore ofOpenAI(
            FluentLogger logger,
            Optional<String> model
    ) {
        Optional<String> namespace = Optional.empty();
        Optional<String> textKey = Optional.empty();

        PineconeServiceConfig config = PineconeServiceConfig
                .builder()
                .setEndpoint(ApiEndPointUtils.getPineconeEndPointFromEnv(Optional.ofNullable(logger)))
                .setApiKey(ApiKeyUtils.getPineconeKeyFromEnv(Optional.ofNullable(logger)))
                .build();
        return ofOpenAI(new PineconeService(config), namespace, textKey, model);
    }

    /**
     * Adds the specified documents to the Pinecone vector store database.
     */
    public UpsertResponse addDocuments(List<DomainDocument> documents) {
        if (documents.isEmpty()) return null;
        List<Vector> vectors = documents.stream()
                .map(this::createVector)
                .collect(Collectors.toCollection(ArrayList::new));

        return addVectors(vectors);
    }

    /**
     * Adds a list of vectors to the Pinecone vector store database.
     */
    private UpsertResponse addVectors(List<Vector> vectors) {
        UpsertRequest.UpsertRequestBuilder upsertRequestBuilder = UpsertRequest.builder()
                        .setVectors(vectors);
        namespace.ifPresent(upsertRequestBuilder::setNamespace);
        return pineconeService.upsert(upsertRequestBuilder.build());
    }

    /**
     * Creates an instance of Vector from given DomainDocument
     * @param document the document from which a Vector will be created
     * @return an instance of {@link Vector}
     */
    private Vector createVector(DomainDocument document) {
        String text = document.getPageContent();
        EmbeddingInput embeddingInput = new EmbeddingInput(model.orElse(DEFAULT_MODEL), Collections.singletonList(text), null);
        EmbeddingOutput embeddingOutput = embeddingsProcessor.run(embeddingInput);
        return Vector.builder()
                .setId(document.getId().orElse(UUID.randomUUID().toString()))
                .setMetadata(document.getMetadata().orElse(Metadata.create()).getValue())
                .setValues(embeddingOutput.getValue().get(0).getVector())
                .build();
    }

    /**
     Performs a similarity search using a vector query and returns a list of pairs containing the domain documents
     and their corresponding similarity scores.
     @param query The vector query to be used for similarity search.
     @param k The number of top results to retrieve.
     @param filter An optional map of filters to be applied during the search. Can be null.
     @return A list of pairs, where each pair consists of a DomainDocument and its similarity score.
     @throws IllegalStateException if both filter and this.filter are provided.
     */
    public List<Pair<DomainDocument, Double>> similaritySearchVectorWithScore(List<Double> query, Long k, @Nullable Map<String, String> filter) {

        if (filter != null && this.filter != null) {
            throw new IllegalStateException("cannot provide both `filter` and `this.filter`");
        }

        QueryRequest.QueryRequestBuilder requestBuilder = QueryRequest.builder()
                .setIncludeMetadata(true)
                .setTopK(k)
                .setVector(query)
                .setFilter(filter != null ? filter : this.filter);

        namespace.ifPresent(requestBuilder::setNamespace);
        QueryResponse response = pineconeService.query(requestBuilder.build());

        List<Pair<DomainDocument, Double>> result = new ArrayList<>();

        //create mapping of PineCone metadata to domain meta data
        if (response.getMatches() != null) {
            for (Match match : response.getMatches()) {
                if (!textKey.isPresent()) continue;
                Metadata metadata = match.getMetadata() == null ? Metadata.create() : Metadata.create(match.getMetadata());
                String key = textKey.get();
                String pageContent = metadata.getValue().get(key);
                if (match.getScore() != null) {
                    DomainDocument document = new DomainDocument.Builder()
                            .setPageContent(pageContent)
                            .setMetadata(Optional.of(metadata))
                            .setId(Optional.empty())
                            .build();
                    result.add(new Pair<>(document, match.getScore()));
                }
            }
        }

        return result;
    }

    /**
     Creates a PineconeVectorStore from a list of domain documents.
     @param documents The list of domain documents to be added to the vector store.
     @param processor The EmbeddingsOutput processor used to process the documents.
     @param service The PineconeService used to communicate with the Pinecone API.
     @param namespace An optional namespace for the vector store. Can be empty.
     @param textKey An optional key used to retrieve text content from the document's metadata. Can be empty.
     @param model An optional model identifier for the vector store. Can be empty.
     @return A new PineconeVectorStore populated with the provided documents.
     */
    public static PineconeVectorStore fromDocuments(
            List<DomainDocument> documents,
            EmbeddingsProcessor processor,
            PineconeService service,
            Optional<String> namespace,
            Optional<String> textKey,
            Optional<String> model
    ) {
        PineconeVectorStore pineconeVectorStore = new PineconeVectorStore(processor, service, namespace, textKey, model);
        pineconeVectorStore.addDocuments(documents);
        return pineconeVectorStore;
    }

}
