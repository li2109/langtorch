package ai.knowly.langtorch.connector.youtube;

import ai.knowly.langtorch.connector.Connector;
import com.google.common.flogger.FluentLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import javax.inject.Inject;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class YoutubeConnector implements Connector<String> {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  private final YoutubeConnectorOption readOption;

  @Inject
  public YoutubeConnector(@NonNull YoutubeConnectorOption readOption) {
    this.readOption = readOption;
  }


  public static String downloadSubtitlesThenReturnFilePath(String url, String outputDirectory)
      throws IOException, InterruptedException, YoutubeReadException {
    // using ProcessBuilder execute yt-dlp
    ProcessBuilder processBuilder = new ProcessBuilder("yt-dlp", "--write-auto-subs",
        "--skip-download",
        "--output", outputDirectory, url);
    Process process = processBuilder.start();
    // get the file name
    String subtitleFileName = getSubtitleFileNameAndOutputLog(process);

    int exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new YoutubeReadException("yt-dlp command failed with exit code: " + exitCode);
    }
    return subtitleFileName;
  }

  @Nullable
  private static String getSubtitleFileNameAndOutputLog(Process process) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    String subtitleFileName = null;
    StringBuilder log = new StringBuilder();

    while ((line = reader.readLine()) != null) {
      log.append(line).append(System.lineSeparator());
      // check the file name
      if (line.startsWith("[info] Writing video subtitles to:")) {
        subtitleFileName = line.substring("[info] Writing video subtitles to:".length()).trim();
      }
    }
    logger.atInfo().log(log.toString());
    return subtitleFileName;
  }


  @Override
  public Optional<String> read() {
    String subtitlesFilePath = null;
    try {
      subtitlesFilePath = downloadSubtitlesThenReturnFilePath(readOption.getUrl(),
          readOption.getOutputDirectory());
      String subtitles = readSubtitlesFile(subtitlesFilePath);
      if (!subtitles.isEmpty()) {
        return Optional.of(readSubtitlesFile(subtitlesFilePath));
      }
    } catch (YoutubeReadException e) {
      logger.atSevere().withCause(e).log("yt-dlp command failed.");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      logger.atSevere().withCause(e).log("Error reading YouTube.");
    } finally {
      deleteSubtitlesFile(subtitlesFilePath);
    }
    return Optional.empty();

  }

  private void deleteSubtitlesFile(String subtitlesFilePath) {
    if (subtitlesFilePath != null) {
      File file = new File(subtitlesFilePath);
      if (file.exists()) {
        file.delete();
      }
    }
  }

  private String readSubtitlesFile(String subtitlesFilePath) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(subtitlesFilePath));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    return sb.toString();
  }

}
