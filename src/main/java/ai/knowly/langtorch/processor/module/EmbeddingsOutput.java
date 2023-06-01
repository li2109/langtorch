package ai.knowly.langtorch.processor.module;

import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.Embeddings;

/**
 EmbeddingsOutput is a shared interface for embedding output.
 */
public interface EmbeddingsOutput extends Processor<EmbeddingInput, Embeddings> {

}
