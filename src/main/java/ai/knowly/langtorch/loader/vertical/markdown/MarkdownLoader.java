package ai.knowly.langtorch.loader.vertical.markdown;

import ai.knowly.langtorch.loader.Loader;
import com.google.common.flogger.FluentLogger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/** Implementation of DocumentConnector for Md files. */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MarkdownLoader implements Loader<String> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  @NonNull private MarkdownLoadOption readOption;

  public static MarkdownLoader create(MarkdownLoadOption readOption) {
    return new MarkdownLoader(readOption);
  }

  @Override
  public Optional<String> read() {
    try {
      return Optional.of(
          new String(
              Files.readAllBytes(Paths.get(readOption.getFilePath())), StandardCharsets.UTF_8));
    } catch (IOException e) {
      logger.atSevere().withCause(e).log("Error reading Markdown file.");
      throw new MarkdownReadException(e);
    }
  }
}
