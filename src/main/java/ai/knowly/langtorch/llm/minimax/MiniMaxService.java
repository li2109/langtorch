package ai.knowly.langtorch.llm.minimax;

import ai.knowly.langtorch.llm.minimax.schema.MiniMaxApiExecutionException;
import ai.knowly.langtorch.llm.minimax.schema.MiniMaxApiServiceInterruptedException;
import ai.knowly.langtorch.llm.minimax.schema.config.MiniMaxServiceConfig;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.completion.ChatCompletionResult;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingRequest;
import ai.knowly.langtorch.llm.minimax.schema.dto.embedding.EmbeddingResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author maxiao
 * @date 2023/06/07
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MiniMaxService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api.minimax.chat";

  private final MiniMaxApi miniMaxApi;

  public static MiniMaxService create(final MiniMaxApi api) {
    return new MiniMaxService(api);
  }

  public static MiniMaxService create(final MiniMaxServiceConfig miniMaxServiceConfig) {
    ObjectMapper defaultObjectMapper = defaultObjectMapper();
    OkHttpClient client = buildClient(miniMaxServiceConfig);
    Retrofit retrofit = defaultRetrofit(client, defaultObjectMapper);

    return new MiniMaxService(retrofit.create(MiniMaxApi.class));
  }

  /**
   * Creates a new MiniMaxService that wraps MiniMaxApi
   *
   * @param groupId minimax group_id string
   * @param apiKey minimax api_key string
   * @param timeout http read timeout, Duration.ZERO means no timeout
   * @return
   */
  public static MiniMaxService create(
      final String groupId, final String apiKey, final Duration timeout) {
    return create(
        MiniMaxServiceConfig.builder()
            .setGroupId(groupId)
            .setApiKey(apiKey)
            .setTimeoutDuration(timeout)
            .build());
  }

  public static MiniMaxApi buildApi(String groupId, String apiKey, Duration timeout) {
    ObjectMapper mapper = defaultObjectMapper();
    OkHttpClient client =
        buildClient(
            MiniMaxServiceConfig.builder()
                .setGroupId(groupId)
                .setApiKey(apiKey)
                .setTimeoutDuration(timeout)
                .build());
    Retrofit retrofit = defaultRetrofit(client, mapper);
    return retrofit.create(MiniMaxApi.class);
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
    return execute(miniMaxApi.createChatCompletion(request));
  }

  public ListenableFuture<ChatCompletionResult> createChatCompletionAsync(
      ChatCompletionRequest request) {
    return miniMaxApi.createChatCompletion(request);
  }

  public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
    return execute(miniMaxApi.createEmbeddings(request));
  }

  public ListenableFuture<EmbeddingResult> createEmbeddingsAsync(EmbeddingRequest request) {
    return miniMaxApi.createEmbeddings(request);
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
      throw new MiniMaxApiExecutionException(e);
    }
  }
}
