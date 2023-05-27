package ai.knowly.langtorch.connector.csv;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class CSVConnectorTest {
  @Test
  void testReadCSV() throws IOException {
    // Arrange.
    String testFilePath = "src/test/resources/test.csv";

    // Act.
    String result = CSVConnector.create().read(testFilePath);

    // Assert.
    String expectedContent =
        "CSVRecord [comment='null', recordNumber=1, values=[LatD,  \"LatM\",  \"LatS\",  \"NS\",  \"LonD\",  \"LonM\",  \"LonS\",  \"EW\",  \"City\",  \"State\"]]\n"
            + "CSVRecord [comment='null', recordNumber=2, values=[   41,     5,    59,  \"N\",      80,    39,     0,  \"W\",  \"Youngstown\",  OH]]\n"
            + "CSVRecord [comment='null', recordNumber=3, values=[   42,    52,    48,  \"N\",      97,    23,    23,  \"W\",  \"Yankton\",  SD]]\n"
            + "CSVRecord [comment='null', recordNumber=4, values=[   46,    35,    59,  \"N\",     120,    30,    36,  \"W\",  \"Yakima\",  WA]]\n";
    assertThat(result).isEqualTo(expectedContent);
  }
}
