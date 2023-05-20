package ai.knowly.langtorch.llm.integration.openai.service;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** OkHttp Interceptor that adds an authorization token header */
public class OpenAIAuthenticationInterceptor implements Interceptor {

  private final String token;

  OpenAIAuthenticationInterceptor(String token) {
    Objects.requireNonNull(token, "OpenAI token required");
    this.token = token;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request =
        chain.request().newBuilder().header("Authorization", "Bearer " + token).build();
    return chain.proceed(request);
  }
}
