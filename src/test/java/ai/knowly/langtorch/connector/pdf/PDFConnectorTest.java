package ai.knowly.langtorch.connector.pdf;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

final class PDFConnectorTest {
  @Test
  void testPDFConnector() throws IOException {
    // Arrange.
    // Act.
    Optional<String> actualContent =
        new PDFConnector(
                PDFConnectorOption.builder().setFilePath("src/test/resources/test.pdf").build())
            .read();

    // Assert.
    assertThat(actualContent.get().trim())
        .isEqualTo(
            "This is a test PDF document. \n"
                + "If you can read this, you have Adobe Acrobat Reader installed on your"
                + " computer.");
  }
}
