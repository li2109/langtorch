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
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenAIApi {

  @POST("/v1/completions")
  Single<CompletionResult> createCompletion(@Body CompletionRequest request);

  @POST("/v1/chat/completions")
  Single<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request);

  @POST("/v1/edits")
  Single<EditResult> createEdit(@Body EditRequest request);

  @POST("/v1/embeddings")
  Single<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request);

  @POST("/v1/images/generations")
  Single<ImageResult> createImage(@Body CreateImageRequest request);

  @POST("/v1/images/edits")
  Single<ImageResult> createImageEdit(@Body RequestBody requestBody);

  @POST("/v1/images/variations")
  Single<ImageResult> createImageVariation(@Body RequestBody requestBody);

  @POST("/v1/moderations")
  Single<ModerationResult> createModeration(@Body ModerationRequest request);
}
