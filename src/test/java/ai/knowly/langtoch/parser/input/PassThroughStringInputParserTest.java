package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.parser.output.PassThroughStringOutputParser;
import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;

public class PassThroughStringInputParserTest {
  private PassThroughStringOutputParser parser;

  @Before
  public void setUp() {
    parser = new PassThroughStringOutputParser();
  }

  @Test
  public void parse_validString_returnsSameString() {
    // Arrange
    String input = "Hello, World!";

    // Act
    String result = parser.parse(input);

    // Assert
    Truth.assertThat(result).isEqualTo(input);
  }

  @Test
  public void parse_emptyString() {
    // Arrange
    String input = "";

    // Act
    String result = parser.parse(input);

    // Assert
    Truth.assertThat(result).isEqualTo(input);
  }
}
