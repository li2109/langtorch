package ai.knowly.langtorch.llm.integration.cohere;

import ai.knowly.langtorch.llm.integration.cohere.schema.CohereGenerateRequest;
import ai.knowly.langtorch.llm.integration.cohere.schema.CohereGenerateResponse;
import com.google.common.util.concurrent.ListenableFuture;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CohereApi {
  @POST("/v1/generate")
  ListenableFuture<CohereGenerateResponse> generate(@Body CohereGenerateRequest request);
}
