package ai.knowly.langtorch.loader.vertical.pdf;

import ai.knowly.langtorch.loader.Loader;
import com.google.common.flogger.FluentLogger;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/** Implementation of DocumentConnector for PDF files. */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PDFLoader implements Loader<String> {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  @NonNull PDFLoadOption readOption;

  public static PDFLoader create(PDFLoadOption readOption) {
    return new PDFLoader(readOption);
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
