package ai.knowly.langtorch.connector.youtube;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.jupiter.api.Test;

class YoutubeProviderTest {

  @Test
  void testFailVerifyYoutubeAPIKey(){

  }

  @Test
  void testGetYoutubeService() throws GeneralSecurityException, IOException {
    YoutubeProvider.getService();
  }
}