package ai.knowly.langtorch.llm.huggingface;

import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** OkHttp Interceptor that adds an authorization token header */
public class HuggingFaceAuthenticationInterceptor implements Interceptor {

  private final String token;

  HuggingFaceAuthenticationInterceptor(String token) {
    Objects.requireNonNull(token, "HuggingFace api token required");
    this.token = token;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request =
        chain.request().newBuilder().header("Authorization", "Bearer " + token).build();
    return chain.proceed(request);
  }
}
