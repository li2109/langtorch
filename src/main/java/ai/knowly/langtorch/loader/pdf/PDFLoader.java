package ai.knowly.langtorch.loader.pdf;

import ai.knowly.langtorch.loader.DocumentLoader;
import com.google.common.flogger.FluentLogger;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/** Implementation of DocumentConnector for PDF files. */
public class PDFLoader extends DocumentLoader<PDFLoadOption> {
  FluentLogger logger = FluentLogger.forEnclosingClass();

  private PDFLoader() {}

  public static PDFLoader create() {
    return new PDFLoader();
  }

  public String read(String filePath) throws IOException {
    return read(PDFLoadOption.builder().setFilePath(filePath).build());
  }

  @Override
  protected String read(PDFLoadOption readOption) throws IOException {
    PDDocument document = PDDocument.load(new File(readOption.getFilePath()));
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text = pdfStripper.getText(document);
    document.close();
    return text;
  }
}
