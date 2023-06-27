package ai.knowly.langtorch.store.vectordb.integration.pgvector;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StringBuilderUtils {

  public static void trimSqlQueryParameter(StringBuilder stringBuilder) {
    int index = stringBuilder.lastIndexOf(", ");
    if (index > 0) stringBuilder.delete(index, stringBuilder.length());
  }
}
