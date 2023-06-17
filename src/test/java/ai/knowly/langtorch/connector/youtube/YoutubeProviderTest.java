package ai.knowly.langtorch.connector.youtube;

import static ai.knowly.langtorch.loader.youtube.YoutubeProvider.getService;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.knowly.langtorch.loader.markdown.MarkdownLoader;
import ai.knowly.langtorch.loader.youtube.YoutubeProvider;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Sleeper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CaptionListResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

class YoutubeProviderTest {

  @Test
  void testGoogleAuthorityTimeout()
      throws GeneralSecurityException, IOException {
    // Arrange.
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    // Act.
    TimeoutException e = assertThrows(TimeoutException.class,
        () -> {
          CompletableFuture<Credential> future = YoutubeProvider.authorizeAsync(httpTransport, 0L);
          Thread.sleep(1000);
          try{
            future.join();
          }catch (CompletionException ce){
            throw ce.getCause();
          }
        });

    // Assert.
    assertThat(e).isInstanceOf(TimeoutException.class);
  }

  @Test
  void testFailVerifyYoutubeAPIKey() {

  }

  @Test
  void testGetYoutubeService() throws GeneralSecurityException, IOException {
    // Arrange.
    // Act.

    // Assert.
    // The authorization should be done in 15(test) second
    assertThat(getService(15)).isNotNull();
  }

  @Test
  void testYoutubeService() throws GeneralSecurityException, IOException {
    // Arrange.
    // Act.

    // Assert.
    // The authorization should be done in 15(test) second
    YouTube youTube = getService(15);
    YouTube.Captions.List request = youTube.captions()
        .list(Arrays.asList("id", "snippet"), "fVW8-px4Ufw");

    CaptionListResponse response = request.execute();
    System.out.println(response);
  }

  @Test
  void testYoutubeService1() throws GeneralSecurityException, IOException {
    // Arrange.
    // Act.

    // Assert.
    // The authorization should be done in 15(test) second
    YouTube youtubeService = getService();
    YouTube.Captions.Download request = youtubeService.captions().download("AUieDaYto-kxqMNILh7HsordgHdZmbcTQsNFQw1GFrXu");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    request.getMediaHttpDownloader();
    request.executeMediaAndDownloadTo(outputStream);
    String subtitles = outputStream.toString(StandardCharsets.UTF_8.name());

    // Print the subtitles
    System.out.println(subtitles);
  }

}