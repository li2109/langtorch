package ai.knowly.langtorch.llm.huggingface;

import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskRequest;
import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskResponse;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.POST;

/** HuggingFaceApi provides the Retrofit interface for the HuggingFace API. */
public interface HuggingFaceApi {
  @POST(".")
  ListenableFuture<List<CreateTextGenerationTaskResponse>> createTextGenerationTask(
      @Body CreateTextGenerationTaskRequest request);
}
