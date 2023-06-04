package ai.knowly.langtorch.connector.youtube;

import ai.knowly.langtorch.connector.DocumentConnector;
import com.google.api.services.youtube.YouTube;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

public class YoutubeConnector extends DocumentConnector<YoutubeReadOption> {

  private final YouTube youtubeService;


  private YoutubeConnector(YouTube youtubeService){
    this.youtubeService = youtubeService;
  };

  public static YoutubeConnector create() throws GeneralSecurityException, IOException {
    YouTube youtube = YoutubeProvider.getService();
    return new YoutubeConnector(youtube);
  }

  public static YoutubeConnector create(YouTube youtubeService) {
    return new YoutubeConnector(youtubeService);
  }

  public String read(String link) throws IOException {
    return read(YoutubeReadOption.builder().setLink(link).build());
  }

  @Override
  protected String read(YoutubeReadOption readOption) throws IOException {
    OutputStream output = new FileOutputStream("YOUR_FILE");

    // Define and execute the API request
    YouTube.Captions.Download request = youtubeService.captions()
        .download("");
    request.getMediaHttpDownloader();
//    request.executeMediaAndDownloadTo(output);
    return null;
  }
}
