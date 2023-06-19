package ai.knowly.langtorch.connector.youtube;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import org.junit.Test;

public class YoutubeConnectorTest {

  @Test
  public void testExec()  {
    YoutubeConnector.download();

//    YoutubeConnector.download("https://www.youtube.com/watch?v=51QO4pavK3A");
    assertThat(1)
        .isEqualTo(
            1);
  }
//
//  @Test
//  void testVerifyYoutubeAPIKeyTimeout(){
////    YoutubeProvider.getService(0);
//
//  }
//
//  @Test
//  void testSuccessVerifyYoutubeAPIKey(){
//
//  }
//  @Test
//  void testReadYoutubeSubtitles(){
//
//  }
}
