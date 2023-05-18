package ai.knowly.langtoch.llm.integration.openai.service;

import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.CompletionResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.completion.chat.ChatCompletionResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.edit.EditRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.edit.EditResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.embedding.EmbeddingRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.embedding.EmbeddingResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.image.CreateImageRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.image.ImageResult;
import ai.knowly.langtoch.llm.integration.openai.service.schema.moderation.ModerationRequest;
import ai.knowly.langtoch.llm.integration.openai.service.schema.moderation.ModerationResult;
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

  @POST("/v1/embeddings")
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
