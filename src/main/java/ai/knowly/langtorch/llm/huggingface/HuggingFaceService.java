package ai.knowly.langtorch.llm.huggingface;

import ai.knowly.langtorch.hub.module.token.EnableOpenAITokenRecord;
import ai.knowly.langtorch.llm.huggingface.exception.HuggingFaceExecutionException;
import ai.knowly.langtorch.llm.huggingface.exception.HuggingFaceHttpException;
import ai.knowly.langtorch.llm.huggingface.exception.HuggingFaceServiceInterruptedException;
import ai.knowly.langtorch.llm.huggingface.schema.config.HuggingFaceServiceConfig;
import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskRequest;
import ai.knowly.langtorch.llm.huggingface.schema.dto.CreateTextGenerationTaskResponse;
import ai.knowly.langtorch.utils.future.retry.FutureRetrier;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/** Service for interacting with the HuggingFace API */
public class HuggingFaceService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api-inference.huggingface.co/models/";

  private final HuggingFaceApi api;
  private final FutureRetrier futureRetrier;

  private final ScheduledExecutorService scheduledExecutor;

  @Inject
  public HuggingFaceService(final HuggingFaceServiceConfig huggingFaceServiceConfig) {
    ObjectMapper defaultObjectMapper = defaultObjectMapper();
    OkHttpClient client = buildClient(huggingFaceServiceConfig);
    Retrofit retrofit =
        defaultRetrofit(huggingFaceServiceConfig.modelId(), client, defaultObjectMapper);
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    this.futureRetrier =
        new FutureRetrier(
            scheduledExecutor,
            huggingFaceServiceConfig.backoffStrategy(),
            huggingFaceServiceConfig.retryConfig());
    this.api = retrofit.create(HuggingFaceApi.class);
  }

  public static <T> T execute(ListenableFuture<T> apiCall) {
    try {
      return apiCall.get();
    } catch (InterruptedException e) {
      // Restore the interrupt status
      Thread.currentThread().interrupt();
      // Optionally, log or handle the exception here.
      logger.atSevere().withCause(e).log("Thread was interrupted during API call.");
      throw new HuggingFaceServiceInterruptedException(e);

    } catch (ExecutionException e) {
      if (e.getCause() instanceof HttpException) {
        HttpException httpException = (HttpException) e.getCause();
        try {
          String errorBody = httpException.response().errorBody().string();
          logger.atSevere().log("HTTP Error: %s", errorBody);
          throw new HuggingFaceHttpException(errorBody);
        } catch (IOException ioException) {
          logger.atSevere().withCause(ioException).log("Error while reading errorBody");
        }
      }
      throw new HuggingFaceExecutionException(e);
    }
  }

  public static ObjectMapper defaultObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(Include.NON_NULL);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    return mapper;
  }

  public static OkHttpClient buildClient(HuggingFaceServiceConfig huggingFaceServiceConfig) {
    return new Builder()
        .addInterceptor(
            new HuggingFaceAuthenticationInterceptor(huggingFaceServiceConfig.apiToken()))
        .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
        .readTimeout(huggingFaceServiceConfig.timeoutDuration().toMillis(), TimeUnit.MILLISECONDS)
        .build();
  }

  public static Retrofit defaultRetrofit(String modelId, OkHttpClient client, ObjectMapper mapper) {
    String url = BASE_URL + modelId + "/";
    return new Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(GuavaCallAdapterFactory.create())
        .build();
  }

  public List<CreateTextGenerationTaskResponse> createTextGenerationTask(
      CreateTextGenerationTaskRequest request) {
    return execute(createChatCompletionAsync(request));
  }

  @EnableOpenAITokenRecord
  public ListenableFuture<List<CreateTextGenerationTaskResponse>> createChatCompletionAsync(
      CreateTextGenerationTaskRequest request) {
    return futureRetrier.runWithRetries(
        () -> api.createTextGenerationTask(request), result -> true);
  }
}
