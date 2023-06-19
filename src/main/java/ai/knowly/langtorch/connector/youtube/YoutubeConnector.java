package ai.knowly.langtorch.connector.youtube;

import ai.knowly.langtorch.connector.Connector;
import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestSubtitlesInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.google.api.services.youtube.YouTube;
import com.google.common.flogger.FluentLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class YoutubeConnector implements Connector<String> {

  FluentLogger logger = FluentLogger.forEnclosingClass();
  private Optional<YouTube> youtubeService;



  private YoutubeConnector(YouTube youtubeService) {
    this.youtubeService = Optional.of(youtubeService);
  }

  ;

  private YoutubeConnector() {
    this.youtubeService = Optional.empty();
  }

  ;


  public static YoutubeConnector create() {
    return new YoutubeConnector();
  }

  public static YoutubeConnector create(YouTube youtubeService) {
    return new YoutubeConnector(youtubeService);
  }

  public static void download(){
    String directory = "/Users/sjx/disk/test";
    String videoUrl = "https://www.youtube.com/watch?v=51QO4pavK3A";
    YoutubeDownloader downloader = new YoutubeDownloader();
    Config config = downloader.getConfig();
    config.setMaxRetries(5);
    Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo("GWGbOjlJDkU"));
    VideoInfo data = response.data();
//    Response<List<SubtitlesInfo>> response1 = downloader
//        .getSubtitlesInfo(new RequestSubtitlesInfo("DFdOcVpRhWI"));
//    List<SubtitlesInfo> data1 = response1.data();
    System.out.println(data.toString());
//    Response<List<SubtitlesInfo>> response = downloader.getSubtitlesInfo(new RequestSubtitlesInfo("DFdOcVpRhWI"));
//    List<SubtitlesInfo> subtitlesInfo = response.data();
//    System.out.println(subtitlesInfo.toString());

//
//    ByteArrayOutputStream susStream = new ByteArrayOutputStream();
//    //接收异常结果流
//    ByteArrayOutputStream errStream = new ByteArrayOutputStream();
//    CommandLine commandLine = CommandLine.parse(command);
//    DefaultExecutor exec =new DefaultExecutor();

//    ExecuteWatchdog watchdog = new ExecuteWatchdog(10000);
//    exec.setWatchdog(watchdog);
//
//    PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
//    exec.setStreamHandler(streamHandler);

  }

  @Override
  public Optional<String> read() throws IOException {

//     Define and execute the API request
    YouTube.Captions.Download request = youtubeService.get().captions().download("");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    request.getMediaHttpDownloader();
    request.executeMediaAndDownloadTo(outputStream);

    // Get the subtitles as a string
    return Optional.of(outputStream.toString(StandardCharsets.UTF_8.name()));
  }

}
