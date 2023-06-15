package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.Connector;
import com.google.common.flogger.FluentLogger;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import lombok.Cleanup;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/** Implementation of DocumentConnector for PDF files. */
public class PDFConnector implements Connector<String> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PDFConnectorOption readOption;

  @Inject
  public PDFConnector(@NonNull PDFConnectorOption readOption) {
    this.readOption = readOption;
  }

  @Override
  public Optional<String> read() {
    try {
      @Cleanup PDDocument document = PDDocument.load(new File(readOption.getFilePath()));
      PDFTextStripper pdfStripper = new PDFTextStripper();
      return Optional.of(pdfStripper.getText(document));
    } catch (IOException e) {
      logger.atSevere().withCause(e).log("Error reading PDF file.");
    }
    return Optional.empty();
  }
}
