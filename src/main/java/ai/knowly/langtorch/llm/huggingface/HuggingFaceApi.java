package ai.knowly.langtorch.llm.huggingface;

import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskRequest;
import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskResponse;
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
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/** HuggingFaceApi provides the Retrofit interface for the HuggingFace API. */
public interface HuggingFaceApi {
  @POST(".")
  ListenableFuture<List<CreateTextGenerationTaskResponse>> createTextGenerationTask(
      @Body CreateTextGenerationTaskRequest request);
}
