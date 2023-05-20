package ai.knowly.langtorch.llm.integration.cohere;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CohereAuthenticationInterceptor implements Interceptor {

  private final String token;

  CohereAuthenticationInterceptor(String token) {
    this.token = token;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request =
        chain
            .request()
            .newBuilder()
            .header("accept", "application/json")
            .header("content-type", "application/json")
            .header("authorization", "Bearer " + token)
            .build();
    return chain.proceed(request);
  }
}
