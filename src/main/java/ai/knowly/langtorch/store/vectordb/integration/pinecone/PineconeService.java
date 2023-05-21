package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.store.vectordb.schema.AddRequest;
import ai.knowly.langtorch.store.vectordb.schema.AddResponse;
import ai.knowly.langtorch.store.vectordb.schema.DeleteRequest;
import ai.knowly.langtorch.store.vectordb.schema.DeleteResponse;
import ai.knowly.langtorch.store.vectordb.schema.QueryRequest;
import ai.knowly.langtorch.store.vectordb.schema.QueryResponse;
import ai.knowly.langtorch.store.vectordb.schema.UpdateRequest;
import ai.knowly.langtorch.store.vectordb.schema.UpdateResponse;
import ai.knowly.langtorch.store.vectordb.schema.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.schema.UpsertResponse;
import ai.knowly.langtorch.store.vectordb.VectorStoreService;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeApiExecutionException;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeHttpParseException;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeInterruptedException;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.PineconeServiceConfig;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.PineconeDeleteRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.PineconeDeleteResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.PineconeFetchResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.PineconeQueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.PineconeQueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.PineconeUpdateRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.PineconeUpdateResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.PineconeUpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.PineconeUpsertResponse;
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

public class PineconeService extends VectorStoreService {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final PineconeAPI api;

  public PineconeService(final PineconeServiceConfig pineconeServiceConfig) {
    ObjectMapper mapper = defaultObjectMapper();
    OkHttpClient client = buildClient(pineconeServiceConfig);
    Retrofit retrofit = defaultRetrofit(pineconeServiceConfig.endpoint(), client, mapper);

    this.api = retrofit.create(PineconeAPI.class);
  }

  public PineconeService(final PineconeAPI api) {
    this.api = api;
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

  public PineconeUpsertResponse upsert(PineconeUpsertRequest request) {
    return execute(api.upsert(request));
  }

  public ListenableFuture<PineconeUpsertResponse> upsertAsync(PineconeUpsertRequest request) {
    return api.upsert(request);
  }

  public PineconeQueryResponse query(PineconeQueryRequest request) {
    return execute(api.query(request));
  }

  public ListenableFuture<PineconeQueryResponse> queryAsync(PineconeQueryRequest request) {
    return api.query(request);
  }

  public PineconeDeleteResponse delete(PineconeDeleteRequest request) {
    return execute(api.delete(request));
  }

  public ListenableFuture<PineconeDeleteResponse> queryAsync(PineconeDeleteRequest request) {
    return api.delete(request);
  }

  public PineconeFetchResponse fetch(FetchRequest request) {
    return execute(api.fetch(request.getNamespace(), request.getIds()));
  }

  public ListenableFuture<PineconeFetchResponse> fetchAsync(FetchRequest request) {
    return api.fetch(request.getNamespace(), request.getIds());
  }

  public PineconeUpdateResponse update(PineconeUpdateRequest request) {
    return execute(api.update(request));
  }

  public ListenableFuture<PineconeUpdateResponse> updateAsync(PineconeUpdateRequest request) {
    return api.update(request);
  }

  @Override
  public AddResponse add(AddRequest request) {
    return upsert(request);
  }

  @Override
  public UpdateResponse update(UpdateRequest request) {
    return null;
  }

  @Override
  public UpsertResponse upsert(UpsertRequest request) {
    return null;
  }

  @Override
  public DeleteResponse delete(DeleteRequest request) {
    return null;
  }

  @Override
  public QueryResponse query(QueryRequest request) {
    return null;
  }
}
