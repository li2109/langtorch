package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.DeleteRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.delete.DeleteResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.fetch.FetchResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.query.QueryResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.UpdateRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.update.UpdateResponse;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertRequest;
import ai.knowly.langtorch.store.vectordb.integration.pinecone.schema.dto.upsert.UpsertResponse;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface PineconeAPI {
  @POST("/vectors/upsert")
  ListenableFuture<UpsertResponse> upsert(@Body UpsertRequest request);

  @POST("/query")
  ListenableFuture<QueryResponse> query(@Body QueryRequest request);

  @POST("/vectors/delete")
  ListenableFuture<DeleteResponse> delete(@Body DeleteRequest request);

  @GET("/vectors/fetch")
  ListenableFuture<FetchResponse> fetch(
      @Query("namespace") String namespace, @Query("ids") List<String> ids);

  @POST("/vectors/update")
  ListenableFuture<UpdateResponse> update(@Body UpdateRequest request);
}
