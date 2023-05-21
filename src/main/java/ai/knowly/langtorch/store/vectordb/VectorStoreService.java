package ai.knowly.langtorch.store.vectordb;

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
import ai.knowly.langtorch.store.vectordb.schema.VectorStoreProviderConfig;

/** Interface for a generic vector store. */
public abstract class VectorStoreService {
  public abstract AddResponse add(AddRequest request);

  public abstract UpdateResponse update(UpdateRequest request);

  public abstract UpsertResponse upsert(UpsertRequest request);

  public abstract DeleteResponse delete(DeleteRequest request);

  public abstract QueryResponse query(QueryRequest request);

  public abstract VectorStoreService withVectorStoreProviderConfig(
      VectorStoreProviderConfig config);
}
