package ai.knowly.langtorch.store.vectordb.integration.pgvector;

import ai.knowly.langtorch.processor.EmbeddingProcessor;
import ai.knowly.langtorch.processor.openai.OpenAIServiceProvider;
import ai.knowly.langtorch.processor.openai.embedding.OpenAIEmbeddingProcessor;
import ai.knowly.langtorch.processor.openai.embedding.OpenAIEmbeddingsProcessorConfig;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;
import ai.knowly.langtorch.schema.io.DomainDocument;
import ai.knowly.langtorch.schema.io.Metadata;
import ai.knowly.langtorch.store.vectordb.integration.EmbeddingProcessorType;
import ai.knowly.langtorch.store.vectordb.integration.EmbeddingProcessorTypeNotFound;
import ai.knowly.langtorch.store.vectordb.integration.VectorStore;
import ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.PGVectorQueryParameters;
import ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.PGVectorStoreSpec;
import ai.knowly.langtorch.store.vectordb.integration.pgvector.schema.PGVectorValues;
import ai.knowly.langtorch.store.vectordb.integration.schema.SimilaritySearchQuery;
import com.pgvector.PGvector;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PGVectorStore implements VectorStore {

    private static final int EMBEDDINGS_COLUMN_COUNT = 2;
    private static final int EMBEDDINGS_INDEX_ID = 0;
    private static final int EMBEDDINGS_INDEX_VECTOR = 1;
    private static final int METADATA_COLUMN_COUNT = 4;
    private static final int METADATA_INDEX_ID = 0;
    private static final int METADATA_INDEX_KEY = 1;
    private static final int METADATA_INDEX_VALUE = 2;
    private static final int METADATA_INDEX_VECTOR_ID = 3;


    @NonNull
    private final EmbeddingProcessor embeddingsProcessor;
    private final PGVectorStoreSpec pgVectorStoreSpec;

    private final SqlCommandProvider sqlCommandProvider;

    public PGVectorStore(@NonNull EmbeddingProcessor embeddingsProcessor, PGVectorStoreSpec pgVectorStoreSpec) {
        this.embeddingsProcessor = embeddingsProcessor;
        this.pgVectorStoreSpec = pgVectorStoreSpec;
        sqlCommandProvider = new SqlCommandProvider(pgVectorStoreSpec.getDatabaseName());
        init();
    }

    public static PGVectorStore of(
            EmbeddingProcessorType embeddingProcessorType,
            PGVectorStoreSpec spec
    ) {
        Optional<EmbeddingProcessor> processor;
        if (embeddingProcessorType == EmbeddingProcessorType.OPENAI) {
            OpenAIEmbeddingsProcessorConfig config = OpenAIEmbeddingsProcessorConfig.builder()
                    .build();
            processor = Optional.of(new OpenAIEmbeddingProcessor(OpenAIServiceProvider.createOpenAIService(), config));
        } else {
            throw new EmbeddingProcessorTypeNotFound(
                    String.format("Embedding processor type %s is not supported.", embeddingProcessorType.name())
            );
        }

        return new PGVectorStore(processor.get(), spec);
    }

    private void init() {
        try {
            createEmbeddingsTable();
            createMetadataTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Adds a list of documents to the PGVector database.
     *
     * @return true if vectors added successfully, otherwise false
     */
    @Override
    public boolean addDocuments(List<DomainDocument> documents) {
        if (documents.isEmpty()) {
            throw new IllegalStateException("Attempted to add an empty list");
        }

        PGVectorQueryParameters pgVectorQueryParameters = getVectorQueryParameters(documents);
        List<PGVectorValues> vectorValues = pgVectorQueryParameters.getVectorValues();
        PGVectorService pgVectorService = pgVectorStoreSpec.getPgVectorService();

        PreparedStatement insertEmbeddingsStmt;
        PreparedStatement insertMetadataStmt;
        int result;
        int metadataResult;
        try {
            insertEmbeddingsStmt = pgVectorService.prepareStatement(
                    sqlCommandProvider.getInsertEmbeddingsQuery(pgVectorQueryParameters.getVectorParameters())
            );
            insertMetadataStmt = pgVectorService.prepareStatement(
                    sqlCommandProvider.getInsertMetadataQuery(pgVectorQueryParameters.getMetadataParameters())
            );
            setQueryParameters(vectorValues, insertEmbeddingsStmt, insertMetadataStmt);
            result = insertEmbeddingsStmt.executeUpdate();
            metadataResult = insertMetadataStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result == vectorValues.size() && metadataResult == pgVectorQueryParameters.getMetadataSize();
    }

    /**
     * Performs a similarity search using a vector query and returns a list of pairs containing the
     * schema documents and their corresponding similarity scores.
     */
    @Override
    public List<Pair<DomainDocument, Double>> similaritySearchVectorWithScore(
            SimilaritySearchQuery similaritySearchQuery
    ) {
        float[] queryVectorValuesAsFloats = getFloatVectorValues(similaritySearchQuery.getQuery());
        double[] queryVectorValuesAsDoubles = getDoubleVectorValues(queryVectorValuesAsFloats);
        PGVectorService pgVectorService = pgVectorStoreSpec.getPgVectorService();
        List<Pair<DomainDocument, Double>> documentsWithScores;
        try {
            PreparedStatement neighborStmt = pgVectorService.prepareStatement(
                    sqlCommandProvider.getSelectEmbeddingsQuery(
                            pgVectorStoreSpec.getDistanceStrategy().getValue(),
                            similaritySearchQuery.getTopK()
                    )
            );

            neighborStmt.setObject(1, new PGvector(queryVectorValuesAsFloats));
            ResultSet result = neighborStmt.executeQuery();
            Map<String, Pair<DomainDocument, Double>> documentsWithScoresMap = new LinkedHashMap<>();

            while (result.next()) {
                String vectorId = (String) result.getObject(1);
                PGvector pGvector = (PGvector) result.getObject(2);
                String key = (String) result.getObject(3);
                String value = (String) result.getObject(4);

                double[] currentVector = getDoubleVectorValues(pGvector.toArray());

                double score = pgVectorStoreSpec.getDistanceStrategy()
                        .calculateDistance(queryVectorValuesAsDoubles, currentVector);

                documentsWithScoresMap.computeIfAbsent(vectorId, s -> {
                    Metadata defaultMetadata = Metadata.builder().build();
                    DomainDocument defaultDocument = DomainDocument.builder()
                            .setId(vectorId)
                            .setPageContent("")
                            .setMetadata(defaultMetadata)
                            .build();
                    return Pair.of(defaultDocument, score);
                });

                Pair<DomainDocument, Double> documentWithScore = documentsWithScoresMap.get(vectorId);
                saveValueToMetadataIfPresent(documentWithScore.getLeft(), key, value);
                documentsWithScoresMap.put(vectorId, getDocumentWithScoreWithPageContent(documentWithScore, key, value));
            }
            documentsWithScores = new ArrayList<>(documentsWithScoresMap.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return documentsWithScores;
    }

    private void createEmbeddingsTable() throws SQLException {
        pgVectorStoreSpec.getPgVectorService().executeUpdate(
                sqlCommandProvider.getCreateEmbeddingsTableQuery(pgVectorStoreSpec.getVectorDimensions())
        );
    }

    private void createMetadataTable() throws SQLException {
        pgVectorStoreSpec.getPgVectorService().executeUpdate(
                sqlCommandProvider.getCreateMetadataTableQuery()
        );
    }

    private PGVectorQueryParameters getVectorQueryParameters(List<DomainDocument> documents) {
        List<PGVectorValues> vectorValues = new ArrayList<>();
        StringBuilder vectorParameters = new StringBuilder();
        StringBuilder metadataParameters = new StringBuilder();
        int metadataSize = 0;
        for (DomainDocument document : documents) {
            List<Double> vector = createVector(document);
            String id = document.getId().orElse(UUID.randomUUID().toString());

            vectorValues.add(
                    PGVectorValues.builder()
                            .setId(id)
                            .setValues(getFloatVectorValues(vector))
                            .setMetadata(document.getMetadata())
                            .build()
            );
            vectorParameters.append("(?, ?), "); //document id and vector

            Optional<Metadata> metadata = document.getMetadata();
            if (metadata.isPresent()) {
                metadataSize += metadata.get().getValue().size();
                for (Map.Entry<String, String> entry : metadata.get().getValue().entrySet()) {
                    metadataParameters.append("(?, ?, ?, ?), "); //id, key, value and document id
                }
            }
        }
        int index = vectorParameters.lastIndexOf(", ");
        if (index > 0) vectorParameters.delete(index, vectorParameters.length());
        index = metadataParameters.lastIndexOf(", ");
        if (index > 0) metadataParameters.delete(index, metadataParameters.length());
        return PGVectorQueryParameters.builder()
                .setVectorValues(vectorValues)
                .setVectorParameters(vectorParameters.toString())
                .setMetadataParameters(metadataParameters.toString())
                .setMetadataSize(metadataSize)
                .build();
    }

    private List<Double> createVector(DomainDocument document) {
        EmbeddingOutput embeddingOutput =
                embeddingsProcessor.run(
                        EmbeddingInput.builder()
                                .setModel(pgVectorStoreSpec.getModel())
                                .setInput(Collections.singletonList(document.getPageContent()))
                                .build());
        return embeddingOutput.getValue().get(0).getVector();
    }

    private int setMetadataQueryParameters(
            PGVectorValues values,
            int parameterIndex,
            PreparedStatement insertStmt
    ) throws SQLException {
        Optional<Metadata> metadata = values.getMetadata();
        if (!metadata.isPresent()) return parameterIndex;
        for (Map.Entry<String, String> entry : metadata.get().getValue().entrySet()) {
            for (int j = 0; j < METADATA_COLUMN_COUNT; j++) {
                switch (j) {
                    case METADATA_INDEX_ID:
                        String id = values.getId() + entry.getKey();
                        insertStmt.setString(parameterIndex, id);
                        break;
                    case METADATA_INDEX_KEY:
                        insertStmt.setString(parameterIndex, entry.getKey());
                        break;
                    case METADATA_INDEX_VALUE:
                        insertStmt.setString(parameterIndex, entry.getValue());
                        break;
                    case METADATA_INDEX_VECTOR_ID:
                        insertStmt.setString(parameterIndex, values.getId());
                        break;
                }
                parameterIndex++;
            }
        }
        return parameterIndex;
    }

    private int setVectorQueryParameters(
            PGVectorValues values,
            int parameterIndex,
            PreparedStatement insertStmt
    ) throws SQLException {
        for (int i = 0; i < EMBEDDINGS_COLUMN_COUNT; i++) {
            switch (i) {
                case EMBEDDINGS_INDEX_ID:
                    insertStmt.setString(parameterIndex, values.getId());
                    break;
                case EMBEDDINGS_INDEX_VECTOR:
                    insertStmt.setObject(parameterIndex, new PGvector(values.getValues()));
                    break;
            }
            parameterIndex++;
        }
        return parameterIndex;
    }

    private void setQueryParameters(
            List<PGVectorValues> vectorValues,
            PreparedStatement insertEmbeddingsStmt,
            PreparedStatement insertMetadataStmt
    ) throws SQLException {
        int embeddingParameterIndex = 1;
        int metadataParameterIndex = 1;
        for (PGVectorValues values : vectorValues) {
            embeddingParameterIndex = setVectorQueryParameters(values, embeddingParameterIndex, insertEmbeddingsStmt);
            metadataParameterIndex = setMetadataQueryParameters(values, metadataParameterIndex, insertMetadataStmt);
        }
    }

    private void saveValueToMetadataIfPresent(DomainDocument document, String key, String value) {
        Optional<Metadata> metadata = document.getMetadata();

        if (!metadata.isPresent() || key == null) return;

        metadata.get().getValue().put(key, value);
    }

    private Pair<DomainDocument, Double> getDocumentWithScoreWithPageContent(
            Pair<DomainDocument, Double> documentWithScore,
            String key,
            String value
    ) {
        if (key == null) return documentWithScore;
        Optional<String> textKey = pgVectorStoreSpec.getTextKey();

        if (!textKey.isPresent()) return documentWithScore;

        boolean isTextKey = key.equals(textKey.get());
        if (!isTextKey) return documentWithScore;

        DomainDocument document = documentWithScore.getLeft().toBuilder()
                .setPageContent(value)
                .build();
        return Pair.of(document, documentWithScore.getRight());
    }

    private float[] getFloatVectorValues(List<Double> vectorValues) {
        float[] floats = new float[vectorValues.size()];
        int i = 0;
        for (Double vectorValue : vectorValues) {
            floats[i] = vectorValue.floatValue();
            i++;
        }
        return floats;
    }

    private double[] getDoubleVectorValues(float[] vectorValues) {
        double[] doubles = new double[vectorValues.length];
        for (int i = 0; i < vectorValues.length; i++) {
            doubles[i] = vectorValues[i];
        }
        return doubles;
    }
}