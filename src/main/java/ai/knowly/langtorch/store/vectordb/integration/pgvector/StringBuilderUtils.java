package ai.knowly.langtorch.store.vectordb.integration.pgvector;

public class StringBuilderUtils {

    public static void trimSqlQueryParameter(StringBuilder stringBuilder) {
        int index = stringBuilder.lastIndexOf(", ");
        if (index > 0) stringBuilder.delete(index, stringBuilder.length());
    }
}
