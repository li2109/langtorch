package ai.knowly.langtorch.llm.integration.cohere;

import ai.knowly.langtorch.llm.integration.cohere.schema.CohereGenerateRequest;
import ai.knowly.langtorch.llm.integration.cohere.schema.CohereGenerateResponse;
import ai.knowly.langtorch.llm.integration.cohere.schema.CohereHttpException;
import ai.knowly.langtorch.llm.integration.cohere.serialization.CohereGenerateRequestAdapter;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CohereAPIService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String BASE_URL = "https://api.cohere.ai/";
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(CohereGenerateRequest.class, new CohereGenerateRequestAdapter())
          .create();

  private final CohereApi api;

  /**
   * Creates a new CohereAPIService that wraps CohereApi
   *
   * @param token Cohere token string
   */
  public CohereAPIService(final String token) {
    this(token, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a new CohereAPIService that wraps CohereApi
   *
   * @param token Cohere token string
   * @param timeout http read timeout, Duration.ZERO means no timeout
   */
  public CohereAPIService(final String token, final Duration timeout) {
    this(buildApi(token, timeout));
  }

  public CohereAPIService(final CohereApi api) {
    this.api = api;
  }

  public static <T> T execute(ListenableFuture<T> apiCall) {
    try {
      return apiCall.get();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof HttpException) {
        HttpException httpException = (HttpException) e.getCause();
        try {
          String errorBody = httpException.response().errorBody().string();
          logger.atSevere().log("HTTP Error: %s", errorBody);
          throw new CohereHttpException(errorBody, httpException);
        } catch (IOException ioException) {
          logger.atSevere().withCause(ioException).log("Error while reading errorBody");
        }
      }
      throw new RuntimeException(e);
    }
  }

  public static CohereApi buildApi(String token, Duration timeout) {
    Objects.requireNonNull(token, "OpenAI token required");
    OkHttpClient client = defaultClient(token, timeout);
    Retrofit retrofit = defaultRetrofit(client, gson);
    return retrofit.create(CohereApi.class);
  }

  public static OkHttpClient defaultClient(String token, Duration timeout) {
    return new OkHttpClient.Builder()
        .addInterceptor(new CohereAuthenticationInterceptor(token))
        .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
        .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
        .build();
  }

  public static Retrofit defaultRetrofit(OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(GuavaCallAdapterFactory.create())
        .build();
  }

  public CohereGenerateResponse generate(CohereGenerateRequest request) {
    return execute(api.generate(request));
  }

  public ListenableFuture<CohereGenerateResponse> generateAsync(CohereGenerateRequest request) {
    return api.generate(request);
  }
}
