package ai.knowly.langtorch.llm.minimax;

import ai.knowly.langtorch.llm.minimax.schema.MiniMaxApiBusinessErrorException;
import ai.knowly.langtorch.llm.minimax.schema.MiniMaxApiExecutionException;
import ai.knowly.langtorch.llm.minimax.schema.MiniMaxApiServiceInterruptedException;
import ai.knowly.langtorch.llm.minimax.schema.config.MiniMaxServiceConfig;
import ai.knowly.langtorch.llm.minimax.schema.dto.BaseResp;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionResult;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import ai.knowly.langtorch.utils.future.retry.FutureRetrier;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * MiniMaxService wraps MiniMaxApi and provides a synchronous and asynchronous interface to the
 * MiniMax API
 *
 * @author maxiao
 * @date 2023/06/07
 */
public class MiniMaxService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api.minimax.chat";

  private final MiniMaxApi api;
  private final FutureRetrier futureRetrier;

  private final ScheduledExecutorService scheduledExecutor;

  @Inject
  public MiniMaxService(final MiniMaxServiceConfig miniMaxServiceConfig) {
    ObjectMapper defaultObjectMapper = defaultObjectMapper();
    OkHttpClient client = buildClient(miniMaxServiceConfig);
    Retrofit retrofit = defaultRetrofit(client, defaultObjectMapper);
    scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    this.futureRetrier =
        new FutureRetrier(
            scheduledExecutor,
            miniMaxServiceConfig.backoffStrategy(),
            miniMaxServiceConfig.retryConfig());
    this.api = retrofit.create(MiniMaxApi.class);
  }

  public static Retrofit defaultRetrofit(OkHttpClient client, ObjectMapper mapper) {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(GuavaCallAdapterFactory.create())
        .build();
  }

  public static OkHttpClient buildClient(MiniMaxServiceConfig miniMaxServiceConfig) {
    OkHttpClient.Builder builder =
        new OkHttpClient.Builder()
            .addInterceptor(
                new MiniMaxAuthenticationInterceptor(
                    miniMaxServiceConfig.groupId(), miniMaxServiceConfig.apiKey()))
            .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
            .readTimeout(miniMaxServiceConfig.timeoutDuration().toMillis(), TimeUnit.MILLISECONDS);

    return builder.build();
  }

  public static ObjectMapper defaultObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    return mapper;
  }

  public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
    ChatCompletionResult chatCompletionResult =
        execute(
            futureRetrier.runWithRetries(() -> api.createChatCompletion(request), result -> true));

    checkResp(chatCompletionResult.getBaseResp());
    return chatCompletionResult;
  }

  public ListenableFuture<ChatCompletionResult> createChatCompletionAsync(
      ChatCompletionRequest request) {
    return futureRetrier.runWithRetries(() -> api.createChatCompletion(request), result -> true);
  }

  public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
    EmbeddingResult embeddingResult =
        execute(futureRetrier.runWithRetries(() -> api.createEmbeddings(request), result -> true));

    checkResp(embeddingResult.getBaseResp());
    return embeddingResult;
  }

  public ListenableFuture<EmbeddingResult> createEmbeddingsAsync(EmbeddingRequest request) {
    return futureRetrier.runWithRetries(() -> api.createEmbeddings(request), result -> true);
  }

  /** Throw exception messages if the request fails */
  public void checkResp(BaseResp baseResp) {
    if (baseResp.getStatusCode() != 0) {
      throw new MiniMaxApiBusinessErrorException(baseResp.getStatusCode(), baseResp.getStatusMsg());
    }
  }

  /**
   * Calls the MiniMax AI api, returns the response, and parses error messages if the request fails
   */
  public static <T> T execute(ListenableFuture<T> apiCall) {
    try {
      return apiCall.get();
    } catch (InterruptedException e) {
      // Restore the interrupt status
      Thread.currentThread().interrupt();
      // Optionally, log or handle the exception here.
      logger.atSevere().withCause(e).log("Thread was interrupted during API call.");
      throw new MiniMaxApiServiceInterruptedException(e);

    } catch (ExecutionException e) {
      if (e.getCause() instanceof HttpException) {
        HttpException httpException = (HttpException) e.getCause();
        try {
          String errorBody = httpException.response().errorBody().string();
          logger.atSevere().log("HTTP Error: %s", errorBody);
        } catch (IOException ioException) {
          logger.atSevere().withCause(ioException).log("Error while reading errorBody");
        }
      }

      throw new MiniMaxApiExecutionException(e);
    }
  }
}
