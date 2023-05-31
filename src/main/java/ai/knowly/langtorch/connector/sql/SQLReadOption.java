package ai.knowly.langtorch.connector.sql;

import ai.knowly.langtorch.connector.ReadOption;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
public class SQLReadOption<S extends StorageObject> implements ReadOption {
  private String query;
  private StorageObjectTransformFunction<S> storageObjectTransformFunction;
}
