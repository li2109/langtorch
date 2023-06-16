package ai.knowly.langtorch.connector.pdf;

import ai.knowly.langtorch.connector.Connector;
import com.google.common.flogger.FluentLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;

import lombok.Cleanup;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Implementation of DocumentConnector for PDF files.
 */
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
            PDDocument selectedDocument;
            Optional<byte[]> bytes = readOption.getBytes();
            Optional<String> filePath = readOption.getFilePath();
            if (bytes.isPresent()) {
                selectedDocument = PDDocument.load(bytes.get());
            } else if (filePath.isPresent()) {
                selectedDocument = PDDocument.load(new File(filePath.get()));
            } else {
                throw new PDFConnectorOptionNotFoundException();
            }

            @Cleanup PDDocument document = selectedDocument;

            PDFTextStripper pdfStripper = new PDFTextStripper();
            return Optional.of(pdfStripper.getText(document));
        } catch (IOException e) {
            logger.atSevere().withCause(e).log("Error reading PDF file.");
        } catch (PDFConnectorOptionNotFoundException e) {
            logger.atSevere().withCause(e).log("No read option provided");
        }
        return Optional.empty();
    }
}
