package ai.knowly.langtorch.llm.minimax;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * OkHttp Interceptor that adds an authorization token header
 *
 * @author maxiao
 * @date 2023/06/07
 */
public class MiniMaxAuthenticationInterceptor implements Interceptor {

  private final String groupId;
  private final String apiKey;

  MiniMaxAuthenticationInterceptor(String groupId, String apiKey) {
    Objects.requireNonNull(groupId, "Minimax groupId required");
    Objects.requireNonNull(apiKey, "Minimax apiKey required");
    this.groupId = groupId;
    this.apiKey = apiKey;
  }

  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {

    HttpUrl url = chain.request().url();
    HttpUrl completeUrl = url.newBuilder().addQueryParameter("GroupId", groupId).build();

    Request request =
        chain
            .request()
            .newBuilder()
            .url(completeUrl)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .build();
    return chain.proceed(request);
  }
}
