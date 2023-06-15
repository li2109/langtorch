package ai.knowly.langtorch.llm.minimax;

import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionResult;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import com.google.common.util.concurrent.ListenableFuture;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * doc link: https://api.minimax.chat/document/guides?id=6433f37194878d408fc8294b
 *
 * @author maxiao
 * @date 2023/06/07
 */
public interface MiniMaxApi {

  @POST("/v1/text/chatcompletion")
  ListenableFuture<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request);

  @POST("/v1/embeddings")
  ListenableFuture<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request);
}
