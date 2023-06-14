package ai.knowly.langtorch.connector.markdown;

import ai.knowly.langtorch.connector.Connector;
import com.google.common.flogger.FluentLogger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import javax.inject.Inject;
import lombok.NonNull;

/** Implementation of DocumentConnector for Md files. */
public class MarkdownConnector implements Connector<String> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final MarkdownConnectorOption readOption;

  @Inject
  public MarkdownConnector(@NonNull MarkdownConnectorOption readOption) {
    this.readOption = readOption;
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
