package ai.knowly.langtoch.parser.output;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.truth.Truth;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StringToSingleVarPromptTemplateOutputParserTest {
  @Test
  public void parseWithContext() {
    StringToSingleVarPromptTemplateOutputParser parser =
        new StringToSingleVarPromptTemplateOutputParser(
            Map.of("template", "Create a name for a {{$area}} company"));
    // Arrange
    String input = "Search Engine";

    // Act
    PromptTemplate result = parser.parse(input);

    // Assert
    String expected = "Create a name for a Search Engine company";
    Truth.assertThat(result.format()).isEqualTo(expected);
  }
}
