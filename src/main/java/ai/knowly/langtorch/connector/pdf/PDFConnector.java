package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.DocumentConnector;
import com.google.common.flogger.FluentLogger;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/** Implementation of DocumentConnector for PDF files. */
public class PDFConnector extends DocumentConnector<PDFConnectorOption> {
  FluentLogger logger = FluentLogger.forEnclosingClass();

  private PDFConnector() {}

  public static PDFConnector create() {
    return new PDFConnector();
  }

  public String read(String filePath) throws IOException {
    return read(PDFConnectorOption.builder().setFilePath(filePath).build());
  }

  @Override
  protected String read(PDFConnectorOption readOption) throws IOException {
    PDDocument document = PDDocument.load(new File(readOption.getFilePath()));
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text = pdfStripper.getText(document);
    document.close();
    return text;
  }
}
