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

    public static PineconeVectorStore ofOpenAI(PineconeServiceConfig config) {
        return new PineconeVectorStore(
                OpenAIEmbeddingsProcessor.create(),
                new PineconeService(config),
                Optional.of("chatbot"),
                Optional.of("text"),
                Optional.empty()
        );
    }

    public static PineconeVectorStore ofOpenAI(FluentLogger logger) {
        PineconeServiceConfig config = PineconeServiceConfig
                .builder()
                .setEndpoint("chatbot-6b96287.svc.asia-southeast1-gcp-free.pinecone.io")
                .setApiKey(ApiKeyUtils.getPineconeKeyFromEnv(Optional.ofNullable(logger)))
                .build();
        return ofOpenAI(config);
    }



    public void addDocuments(List<DomainDocument> documents, @Nullable List<String> ids) {
        List<String> texts = documents.stream().map(DomainDocument::getPageContent).collect(Collectors.toList());
        EmbeddingInput embeddingInput = new EmbeddingInput(model.orElse(DEFAULT_MODEL), texts, null);
        Embeddings embeddings = processor.run(embeddingInput);
        addVectors(embeddings, documents, ids);
    }

    public void addVectors(Embeddings embeddings, List<DomainDocument> documents, @Nullable List<String> ids) {
        List<String> documentIds =
                ids == null ? documents.stream().map(document -> UUID.randomUUID().toString()).collect(Collectors.toList()) : ids;

        List<Vector> pineconeVectors = new ArrayList<>();


        for (int i = 0; i < embeddings.getValue().size(); i++) {
            if (!textKey.isPresent()) continue;

            Embedding embedding = embeddings.getValue().get(i);
            Map<String, String> metadata = new HashMap<>();
            metadata.put(textKey.get(), documents.get(i).getPageContent());
            Vector vector = Vector.builder()
                    .setId(documentIds.get(i))
                    .setMetadata(flattenMetadata(documents.get(i), metadata))
                    .setValues(embedding.getVector())
                    .build();
            pineconeVectors.add(vector);
        }

        UpsertRequest.UpsertRequestBuilder upsertRequestBuilder =
                UpsertRequest.builder()
                        .setVectors(pineconeVectors);

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
                Metadata metadata = match.getMetadata() == null ?  Metadata.create() : Metadata.create(match.getMetadata());
                String key = textKey.get();
                String pageContent = metadata.getValue().get(key, "");
                if (match.getScore() != null) {
                    result.add(new Pair<>(new DomainDocument(pageContent, Optional.of(metadata)), match.getScore()));
                }
            }
        }

        return result;
    }

    public PineconeVectorStore fromTexts(
            List<String> texts,
            List<Map<String, String>> metadatas,
            EmbeddingsProcessor processor,
            PineconeService service,
            Optional<String> namespace,
            Optional<String> textKey) {
        List<DomainDocument> documents = new ArrayList<>();
        for (int i = 0; i < texts.size(); i += 1) {
            documents.add(new DomainDocument(texts.get(i), Optional.of(Metadata.create(metadatas.get(i)))));
        }

        return fromDocuments(documents, processor, service, namespace, textKey, Optional.empty());
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
        pineconeVectorStore.addDocuments(documents, null);
        return pineconeVectorStore;
    }


    // Pinecone doesn't support nested objects, so we must flatten them
    private Map<String, String> flattenMetadata(DomainDocument document, Map<String, String> metaData) {
        Map<String, String> flattenedMetadata = new HashMap<>();
        flattenedMetadata.putAll(document.getMetadata());
        flattenedMetadata.putAll(metaData);
        flattenedMetadata.entrySet().removeIf(entry -> entry.getValue() == null);
        return flattenedMetadata;
    }


}
