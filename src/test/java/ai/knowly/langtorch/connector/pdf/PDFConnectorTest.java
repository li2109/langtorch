package ai.knowly.langtorch.connector.pdf;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.Test;

final class PDFConnectorTest {
  @Test
  void testPDFConnector() {
    // Arrange.
    // Act.
    Optional<String> actualContent =
        new PDFConnector(
                PDFConnectorOption.builder().setFilePath("src/test/resources/test.pdf").build())
            .read();

    // Assert.
    assertThat(actualContent.orElse("").trim())
        .isEqualTo(
            "This is a test PDF document. \n"
                + "If you can read this, you have Adobe Acrobat Reader installed on your"
                + " computer.");
  }

  @Test
  void testPDFConnectorBytes() throws IOException {
    // Arrange.
    // Act.
    Path path = Paths.get("src/test/resources/test.pdf");
    if (Files.exists(path)) {
      byte[] byteArray = Files.readAllBytes(path);
      Optional<String> actualContent =
              new PDFConnector(
                      PDFConnectorOption.builder().setFileBytes(byteArray).build())
                      .read();

      // Assert.
      assertThat(actualContent.get().trim())
              .isEqualTo(
                      "This is a test PDF document. \n"
                              + "If you can read this, you have Adobe Acrobat Reader installed on your"
                              + " computer.");
    }
  }
}
