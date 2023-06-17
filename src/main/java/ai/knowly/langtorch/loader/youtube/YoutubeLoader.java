package ai.knowly.langtorch.loader.youtube;

import ai.knowly.langtorch.loader.DocumentLoader;
import com.google.api.services.youtube.YouTube;
import com.google.common.flogger.FluentLogger;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Optional;

public class YoutubeLoader extends DocumentLoader<YoutubeLoadOption> {
  FluentLogger logger = FluentLogger.forEnclosingClass();
  private Optional<YouTube> youtubeService;


  private YoutubeLoader(YouTube youtubeService){
    this.youtubeService = Optional.of(youtubeService);
  };
  private YoutubeLoader(){
    this.youtubeService = Optional.empty();
  };


  public static YoutubeLoader create(){
    return new YoutubeLoader();
  }

  public static YoutubeLoader create(YouTube youtubeService) {
    return new YoutubeLoader(youtubeService);
  }

  public String read(String link) throws IOException {
    return read(YoutubeLoadOption.builder().setLink(link).build());
  }

  @Override
  protected String read(YoutubeLoadOption readOption) throws IOException {

//     Define and execute the API request
    YouTube.Captions.Download request = youtubeService.get().captions().download("");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    request.getMediaHttpDownloader();
    request.executeMediaAndDownloadTo(outputStream);

    // Get the subtitles as a string
    return outputStream.toString(StandardCharsets.UTF_8.name());
  }
}
