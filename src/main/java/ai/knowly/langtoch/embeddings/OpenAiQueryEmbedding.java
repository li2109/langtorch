package ai.knowly.langtoch.embeddings;

import ai.knowly.langtoch.llm.processor.openai.OpenAIServiceProvider;
import com.google.common.flogger.FluentLogger;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;

import javax.inject.Inject;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static ai.knowly.langtoch.llm.Utils.singleToCompletableFuture;

public class OpenAiQueryEmbedding {

    String modelName = "text-embedding-ada-002";

    boolean stripNewLines = true;


    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final OpenAiApi openAiApi;

    @Inject
    OpenAiQueryEmbedding(OpenAiApi openAiApi) {
        this.openAiApi = openAiApi;
    }

    private OpenAiQueryEmbedding() {
        this.openAiApi = OpenAIServiceProvider.createOpenAiAPI();
    }

    public static OpenAiQueryEmbedding create(String openAIKey) {
        return new OpenAiQueryEmbedding(OpenAIServiceProvider.createOpenAiAPI(openAIKey));
    }

    public static OpenAiQueryEmbedding create() {
        return new OpenAiQueryEmbedding();
    }

    public static OpenAiQueryEmbedding create(OpenAiApi openAiApi) {
        return new OpenAiQueryEmbedding(openAiApi);
    }

    public EmbeddingResult run(String inputData) {
        try {
            return runAsync(CompletableFuture.completedFuture(inputData)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.atWarning().withCause(e).log(
                    "Error running OpenAiQueryEmbedding with input: %s", inputData);
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<EmbeddingResult> runAsync(CompletableFuture<String> inputData) {
        return inputData.thenCompose(
                data -> {
                    EmbeddingRequest request = new EmbeddingRequest();
                    request.setInput(Collections.singletonList(this.stripNewLines ? data.replaceAll("\\n", " ") : data));
                    request.setModel(this.modelName);
                    return singleToCompletableFuture(openAiApi.createEmbeddings(request));
                }
        );
    }
}
