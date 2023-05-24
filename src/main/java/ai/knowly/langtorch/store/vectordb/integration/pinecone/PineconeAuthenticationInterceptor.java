package ai.knowly.langtorch.store.vectordb.integration.pinecone;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** OkHttp Interceptor that adds an authorization header */
public class PineconeAuthenticationInterceptor implements Interceptor {

  private final String apiKey;

  PineconeAuthenticationInterceptor(String apiKey) {
    Objects.requireNonNull(apiKey, "Pinecone API required");
    this.apiKey = apiKey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request =
        chain
            .request()
            .newBuilder()
            .header("accept", "application/json")
            .header("content-type", "application/json")
            .header("Api-Key", apiKey)
            .build();
    return chain.proceed(request);
  }
}
