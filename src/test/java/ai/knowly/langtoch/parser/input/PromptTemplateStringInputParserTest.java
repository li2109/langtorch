package ai.knowly.langtoch.parser.input;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import com.google.common.truth.Truth;
import org.junit.Before;
import org.junit.Test;

public class PromptTemplateStringInputParserTest {
  private PromptTemplateStringInputParser parser;

  @Before
  public void setUp() {
    parser = PromptTemplateStringInputParser.create();
  }

  @Test
  public void parse_validPromptTemplate_noVariableInside() {
    // Arrange
    PromptTemplate promptTemplate = PromptTemplate.builder().setTemplate("Hello, World!").build();

    // Act
    String result = parser.parse(promptTemplate);

    // Assert
    Truth.assertThat(result).isEqualTo("Hello, World!");
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
    String result = parser.parse(promptTemplate);

    // Assert
    Truth.assertThat(result)
        .isEqualTo("I graduated from Purdue University and the campus is in Indiana,United States");
  }
}
