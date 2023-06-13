package ai.knowly.langtorch.processor;

import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;

/** EmbeddingsProcessor is a shared interface for embedding output. */
public interface EmbeddingsProcessor extends Processor<EmbeddingInput, EmbeddingOutput> {}
