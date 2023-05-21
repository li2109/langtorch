package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.PineconeDeleteRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.PineconeDeleteResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.PineconeFetchResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.PineconeQueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.PineconeQueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.PineconeUpdateRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.PineconeUpdateResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.PineconeUpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.PineconeUpsertResponse;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PineconeAPI {

  @POST("/vectors/upsert")
  ListenableFuture<PineconeUpsertResponse> upsert(@Body PineconeUpsertRequest request);

  @POST("/query")
  ListenableFuture<PineconeQueryResponse> query(@Body PineconeQueryRequest request);

  @POST("/vectors/delete")
  ListenableFuture<PineconeDeleteResponse> delete(@Body PineconeDeleteRequest request);

  @GET("/vectors/fetch")
  ListenableFuture<PineconeFetchResponse> fetch(
      @Query("namespace") String namespace, @Query("ids") List<String> ids);

  @POST("/vectors/update")
  ListenableFuture<PineconeUpdateResponse> update(@Body PineconeUpdateRequest request);
}
