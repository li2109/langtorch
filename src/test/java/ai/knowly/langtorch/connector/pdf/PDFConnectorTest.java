package ai.knowly.langtorch.connector.pdf;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;

final class PDFConnectorTest {
  @Test
  void testPDFConnector() throws IOException {
    // Arrange.
    // Act.
    String actualContent = PDFConnector.create().read("src/test/resources/test.pdf");

    // Assert.
    assertThat(actualContent.trim())
        .isEqualTo(
            "This is a test PDF document. \n"
                + "If you can read this, you have Adobe Acrobat Reader installed on your computer.");
  }
}
