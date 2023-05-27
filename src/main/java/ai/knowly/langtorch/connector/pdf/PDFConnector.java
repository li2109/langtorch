package ai.knowly.langtorch.connector.pdf;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/** Implementation of DocumentConnector for PDF files. */
public class PDFConnector extends DocumentConnector {
  private PDFConnector() {}

  public static PDFConnector create() {
    return new PDFConnector();
  }

  @Override
  public String read(String filePath) throws IOException {
    PDDocument document = PDDocument.load(new File(filePath));
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text = pdfStripper.getText(document);
    document.close();
    return text;
  }
}
