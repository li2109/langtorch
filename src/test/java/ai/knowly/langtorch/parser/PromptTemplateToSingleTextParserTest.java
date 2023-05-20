package ai.knowly.langtorch.parser;

import ai.knowly.langtorch.prompt.template.PromptTemplate;
import ai.knowly.langtorch.schema.io.SingleText;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class PromptTemplateToSingleTextParserTest {
  private PromptTemplateToSingleTextParser parser;

  @BeforeEach
  void setUp() {
    parser = PromptTemplateToSingleTextParser.create();
  }

  @Test
  void parse_validPromptTemplate_noVariableInside() {
    // Arrange
    PromptTemplate promptTemplate =
        PromptTemplate.builder().setTemplate("Create a name for search engine company").build();

    // Act
    SingleText result = parser.parse(promptTemplate);

    // Assert
    Truth.assertThat(result.getText()).isEqualTo("Create a name for search engine company");
  }

  @Test
  void parse_validPromptTemplate_threeVariablesInside() {
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
