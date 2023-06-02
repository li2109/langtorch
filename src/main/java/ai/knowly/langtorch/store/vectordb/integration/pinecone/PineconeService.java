package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeApiExecutionException;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeHttpParseException;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeInterruptedException;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.DeleteRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.DeleteResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.UpdateRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.UpdateResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.flogger.FluentLogger;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/** Pinecone llm. */
public class PineconeService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final PineconeAPI api;

  private PineconeService(final PineconeServiceConfig pineconeServiceConfig) {
    ObjectMapper mapper = defaultObjectMapper();
    OkHttpClient client = buildClient(pineconeServiceConfig);
    Retrofit retrofit = defaultRetrofit(pineconeServiceConfig.endpoint(), client, mapper);

    this.api = retrofit.create(PineconeAPI.class);
  }

  private PineconeService(final PineconeAPI api) {
    this.api = api;
  }

  public static PineconeService create(PineconeAPI api) {
    return new PineconeService(api);
  }

  public static PineconeService create(PineconeServiceConfig pineconeServiceConfig) {
    return new PineconeService(pineconeServiceConfig);
  }

  public static <T> T execute(ListenableFuture<T> apiCall) {
    try {
      return apiCall.get();
    } catch (InterruptedException e) {
      // Restore the interrupt status
      Thread.currentThread().interrupt();
      // Optionally, log or handle the exception here.
      logger.atSevere().withCause(e).log("Thread was interrupted during API call.");
      throw new PineconeInterruptedException(e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof HttpException) {
        HttpException httpException = (HttpException) e.getCause();
        try {
          String errorBody = httpException.response().errorBody().string();
          logger.atSevere().log("HTTP Error: %s", errorBody);
          throw new PineconeHttpParseException(errorBody);
        } catch (IOException ioException) {
          logger.atSevere().withCause(ioException).log("Error while reading errorBody");
        }
      }
      throw new PineconeApiExecutionException(e);
    }
  }

  public static ObjectMapper defaultObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    return mapper;
  }

  public static OkHttpClient buildClient(PineconeServiceConfig pineconeServiceConfig) {
    logger.atInfo().log("Pinecone:" + pineconeServiceConfig.apiKey());
    Builder builder =
        new Builder()
            .addInterceptor(new PineconeAuthenticationInterceptor(pineconeServiceConfig.apiKey()))
            .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
            .readTimeout(pineconeServiceConfig.timeoutDuration().toMillis(), TimeUnit.MILLISECONDS);

    if (pineconeServiceConfig.enableLogging()) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      builder.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));
    }
    return builder.build();
  }

  public static Retrofit defaultRetrofit(
      String endpoint, OkHttpClient client, ObjectMapper mapper) {
    return new Retrofit.Builder()
        .baseUrl(endpoint.startsWith("https://") ? endpoint : "https://" + endpoint)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(GuavaCallAdapterFactory.create())
        .build();
  }

  public UpsertResponse upsert(UpsertRequest request) {
    return execute(api.upsert(request));
  }

  public ListenableFuture<UpsertResponse> upsertAsync(UpsertRequest request) {
    return api.upsert(request);
  }

  public QueryResponse query(QueryRequest request) {
    return execute(api.query(request));
  }

  public ListenableFuture<QueryResponse> queryAsync(QueryRequest request) {
    return api.query(request);
  }

  public DeleteResponse delete(DeleteRequest request) {
    return execute(api.delete(request));
  }

  public ListenableFuture<DeleteResponse> queryAsync(DeleteRequest request) {
    return api.delete(request);
  }

  public FetchResponse fetch(FetchRequest request) {
    return execute(api.fetch(request.getNamespace(), request.getIds()));
  }

  public ListenableFuture<FetchResponse> fetchAsync(FetchRequest request) {
    return api.fetch(request.getNamespace(), request.getIds());
  }

  public UpdateResponse update(UpdateRequest request) {
    return execute(api.update(request));
  }

  public ListenableFuture<UpdateResponse> updateAsync(UpdateRequest request) {
    return api.update(request);
  }
}
