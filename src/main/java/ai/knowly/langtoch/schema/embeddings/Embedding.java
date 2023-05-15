package ai.knowly.langtoch.schema.embeddings;

import ai.knowly.langtoch.schema.io.Output;
import com.google.common.collect.ImmutableList;

public abstract class Embedding implements Output {

    private final EmbeddingType type;

    private final ImmutableList<Double> vector;

    abstract Embedding of();

    private Embedding(EmbeddingType type, ImmutableList<Double> vector) {
        this.type = type;
        this.vector = vector;
    }

    public EmbeddingType getType() {
        return type;
    }

    public ImmutableList<Double> getVector() {
        return vector;
    }


}
