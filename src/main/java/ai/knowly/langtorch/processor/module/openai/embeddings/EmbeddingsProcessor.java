package ai.knowly.langtorch.processor.module.openai.embeddings;

import ai.knowly.langtorch.processor.module.Processor;
import ai.knowly.langtorch.schema.embeddings.EmbeddingInput;
import ai.knowly.langtorch.schema.embeddings.Embeddings;

public interface EmbeddingsProcessor extends Processor<EmbeddingInput, Embeddings> {

}
