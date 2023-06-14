package ai.knowly.langtorch.processor.module;

import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.EmbeddingOutput;

/** EmbeddingsProcessor is a shared interface for embedding output. */
public interface EmbeddingProcessor extends Processor<EmbeddingInput, EmbeddingOutput> {}
