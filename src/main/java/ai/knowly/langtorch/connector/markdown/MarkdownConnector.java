package ai.knowly.langtorch.connector.markdown;

import ai.knowly.langtorch.connector.Connector;
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
public class MarkdownConnector implements Connector<String> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  @NonNull private MarkdownConnectorOption readOption;

  public static MarkdownConnector create(MarkdownConnectorOption readOption) {
    return new MarkdownConnector(readOption);
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
