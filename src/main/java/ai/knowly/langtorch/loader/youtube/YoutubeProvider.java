package ai.knowly.langtorch.loader.youtube;

import ai.knowly.langtorch.utils.Environment;
import ai.knowly.langtorch.utils.api.key.ApiKeyEnvUtils;
import ai.knowly.langtorch.utils.api.key.GoogleSecretUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.common.flogger.FluentLogger;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.swing.text.html.Option;

public class YoutubeProvider {
  FluentLogger logger = FluentLogger.forEnclosingClass();
  private final static long AUTHORITY_TIME_OUT=15;
  private static ExecutorService executor;
  private YoutubeProvider(){ };
  static {
    int threadCount = 5;
    executor = Executors.newFixedThreadPool(threadCount);
  }
  private static final Collection<String> SCOPES =
      Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

  private static final String APPLICATION_NAME = "API code samples";
  private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /**
   * Create an authorized Credential object.
   *
   * @return an authorized Credential object.
   * @throws IOException
   */
  private static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
    // Load client secrets.
    String googleSecrets = GoogleSecretUtil.getKey(Environment.TEST);
    InputStream in = new ByteArrayInputStream(googleSecrets.getBytes(StandardCharsets.UTF_8));
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .build();
    Credential credential =
        new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    return credential;
  }
  /**
   * Create an authorized Credential object in async.
   *
   * @return an authorized Credential object.
   * @throws IOException
   */
  public static CompletableFuture<Credential> authorizeAsync(final NetHttpTransport httpTransport, Long timeout) {
    CompletableFuture<Credential> future = new CompletableFuture<>();
    executor.submit(() -> {
      try {
        Credential result = authorize(httpTransport);
        future.complete(result);
      } catch (Exception e) {
        future.completeExceptionally(e);
      }
    });
    TimeUnit timeUnit = TimeUnit.SECONDS;

    scheduleTimeout(future, timeout, timeUnit);
    return future;
  }


  private static <T> void scheduleTimeout(CompletableFuture<T> future, long timeout, TimeUnit timeUnit) {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    scheduler.schedule(() -> {
      if (!future.isDone()) {
        future.completeExceptionally(new TimeoutException("Authentication timed out"));
      }
    }, timeout, timeUnit);

    scheduler.shutdown();
  }

  /**
   * Build and return an authorized API client service.
   *
   * @return an authorized API client service
   * @throws GeneralSecurityException, IOException
   */
  public static YouTube getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    CompletableFuture<Credential> future = authorizeAsync(httpTransport,AUTHORITY_TIME_OUT);
    return new YouTube.Builder(httpTransport, JSON_FACTORY, future.join())
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  /**
   * Build and return an authorized API client service.
   *
   * @return an authorized API client service
   * @throws GeneralSecurityException, IOException
   */
  public static YouTube getService(long timeout) throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    CompletableFuture<Credential> future = authorizeAsync(httpTransport,timeout);
    return new YouTube.Builder(httpTransport, JSON_FACTORY, future.join())
        .setApplicationName(APPLICATION_NAME)
        .build();
  }
  /**
   * shutdown threadPool
   */
  public static void shutdown() {
    executor.shutdown();
  }
}
