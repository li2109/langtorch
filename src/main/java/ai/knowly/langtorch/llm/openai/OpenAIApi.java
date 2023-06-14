package ai.knowly.langtorch.llm.openai;

import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.CompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.completion.chat.ChatCompletionResult;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.edit.EditResult;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.llm.openai.schema.dto.image.CreateImageRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.image.ImageResult;
import ai.knowly.langtorch.llm.openai.schema.dto.moderation.ModerationRequest;
import ai.knowly.langtorch.llm.openai.schema.dto.moderation.ModerationResult;
import com.google.common.util.concurrent.ListenableFuture;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenAIApi {

  @POST("/v1/completions")
  ListenableFuture<CompletionResult> createCompletion(@Body CompletionRequest request);

  @POST("/v1/chat/completions")
  ListenableFuture<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request);

  @POST("/v1/edits")
  ListenableFuture<EditResult> createEdit(@Body EditRequest request);

  @POST("/v1/embedding")
  ListenableFuture<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request);

  @POST("/v1/images/generations")
  ListenableFuture<ImageResult> createImage(@Body CreateImageRequest request);

  @POST("/v1/images/edits")
  ListenableFuture<ImageResult> createImageEdit(@Body RequestBody requestBody);

  @POST("/v1/images/variations")
  ListenableFuture<ImageResult> createImageVariation(@Body RequestBody requestBody);

  @POST("/v1/moderations")
  ListenableFuture<ModerationResult> createModeration(@Body ModerationRequest request);
}
