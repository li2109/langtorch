package ai.knowly.langtoch.schema.embeddings;

import ai.knowly.langtoch.schema.io.Output;

public abstract class Embedding implements Output {

    private final EmbeddingType type;

    private final Double[] vector;

    abstract Embedding of();

    private Embedding(EmbeddingType type, Double[] vector) {
        this.type = type;
        this.vector = vector;
    }

    public EmbeddingType getType() {
        return type;
    }

    public Double[] getVector() {
        return vector;
    }


}
