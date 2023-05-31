package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.processor.module.openai.embeddings.EmbeddingsProcessor;
import ai.knowly.langtorch.processor.module.openai.embeddings.OpenAIEmbeddingsProcessor;
import ai.knowly.langtorch.schema.embeddings.Embedding;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.Embeddings;
import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import ai.knowly.langtorch.store.vectordb.integration.VectorStore;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.Vector;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.Match;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.utils.ApiKeyUtils;
import com.google.common.flogger.FluentLogger;
import kotlin.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PineconeVectorStore implements VectorStore {

    private static final String DEFAULT_MODEL = "text-embedding-ada-002";

    private final EmbeddingsProcessor processor;
    private final PineconeService pineconeService;
    private final Optional<String> namespace;
    private final Optional<String> textKey;
    Map<String, String> filter;
    private final Optional<String> model;

    private PineconeVectorStore(
            EmbeddingsProcessor processor,
            PineconeService service,
            Optional<String> namespace,
            Optional<String> textKey,
            Optional<String> model
    ) {
        this.processor = processor;
        this.pineconeService = service;
        this.namespace = namespace;
        this.textKey = textKey;
        this.model = model;
    }

    public static PineconeVectorStore ofOpenAI(
            PineconeServiceConfig config,
            Optional<String> namespace,
            Optional<String> textKey,
            Optional<String> model
    ) {
        return new PineconeVectorStore(
                OpenAIEmbeddingsProcessor.create(),
                new PineconeService(config),
                namespace,
                textKey,
                model
        );
    }

    public static PineconeVectorStore ofOpenAI(
            FluentLogger logger,
            Optional<String> model
    ) {
        Optional<String> namespace = Optional.of("");
        Optional<String> textKey = Optional.of("");

        PineconeServiceConfig config = PineconeServiceConfig
                .builder()
                .setEndpoint(ApiKeyUtils.getPineconeEndPointFromEnv(Optional.ofNullable(logger)))
                .setApiKey(ApiKeyUtils.getPineconeKeyFromEnv(Optional.ofNullable(logger)))
                .build();
        return ofOpenAI(config, namespace, textKey, model);
    }

    public void addDocuments(List<DomainDocument> documents) {
        documents.forEach(this::addDocument);
    }

    public void addDocument(DomainDocument document) {
        String text = document.getPageContent();
        EmbeddingInput embeddingInput = new EmbeddingInput(model.orElse(DEFAULT_MODEL), Collections.singletonList(text), null);
        Embeddings embeddings = processor.run(embeddingInput);
        addVector(embeddings, document);
    }

    private void addVector(Embeddings embeddings, DomainDocument document) {
        Vector vector = Vector.builder()
                .setId(document.getId().orElse(UUID.randomUUID().toString()))
                .setMetadata(document.getMetadata().orElse(Metadata.create()).getValue())
                .setValues(embeddings.getValue().get(0).getVector())
                .build();
        UpsertRequest.UpsertRequestBuilder upsertRequestBuilder =
                UpsertRequest.builder()
                        .setVectors(Collections.singletonList(vector));
        namespace.ifPresent(upsertRequestBuilder::setNamespace);
        pineconeService.upsert(upsertRequestBuilder.build());
    }

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
                    result.add(new Pair<>(new DomainDocument(pageContent, Optional.of(metadata), Optional.empty()), match.getScore()));
                }
            }
        }

        return result;
    }

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
