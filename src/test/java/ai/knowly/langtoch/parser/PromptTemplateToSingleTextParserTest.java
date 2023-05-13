package ai.knowly.langtoch.parser;

import ai.knowly.langtoch.schema.io.SingleText;
import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;

public class PromptTemplateToSingleTextParserTest {
  private PromptTemplateToSingleTextParser parser;

  @Before
  public void setUp() {
    parser = PromptTemplateToSingleTextParser.create();
  }

  @Test
  public void parse_validPromptTemplate_noVariableInside() {
    // Arrange
    PromptTemplate promptTemplate =
        PromptTemplate.builder().setTemplate("Create a name for search engine company").build();

    // Act
    SingleText result = parser.parse(promptTemplate);

    // Assert
    Truth.assertThat(result.getText()).isEqualTo("Create a name for search engine company");
  }

  @Test
  public void parse_validPromptTemplate_threeVariablesInside() {
    // Arrange
    PromptTemplate promptTemplate =
        PromptTemplate.builder()
            .setTemplate(
                "I graduated from {{$university}} and the campus is in {{$state}},{{$country}}")
            .addVariableValuePair("university", "Purdue University")
            .addVariableValuePair("state", "Indiana")
            .addVariableValuePair("country", "United States")
            .build();

    // Act
    SingleText result = parser.parse(promptTemplate);

    // Assert
    Truth.assertThat(result.getText())
        .isEqualTo("I graduated from Purdue University and the campus is in Indiana,United States");
  }
}
