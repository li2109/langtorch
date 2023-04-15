package ai.knowly.langtoch.prompt.template;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PromptTemplateTest {

  @Test
  public void testPromptTemplateExample() {
    // Arrange.
    String template = "Provide me company names for the {{$industry}}";

    // Act.
    String actual =
        PromptTemplate.builder()
            .setTemplate(template)
            .addVariableValuePair("industry", "technology")
            .setExamples(ImmutableList.of("Search Engine: Google"))
            .build()
            .format();

    // Assert.
    String expected =
        "Provide me company names for the technology\nHere's examples:\nSearch Engine: Google\n";
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testPromptTemplateExample_withCustomizedExampleHeader() {
    // Arrange.
    String template = "Provide me company names for the {{$industry}}";

    // Act.
    String actual =
        PromptTemplate.builder()
            .setTemplate(template)
            .addVariableValuePair("industry", "technology")
            .setExamples(ImmutableList.of("Search Engine: Google", "Social Media: Facebook"))
            .setExampleHeader("Here's two examples:")
            .build()
            .format();

    // Assert.
    String expected =
        "Provide me company names for the technology\nHere's two examples:\nSearch Engine: Google\nSocial Media: Facebook\n";
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testPromptTemplate() {
    // Arrange.
    String template =
        "This is a template for a prompt.\n"
            + "It can be used to test the prompt template.\n"
            + "Name: {{$name}}\n"
            + "Age: {{$age}}\n";

    // Act.
    String actual =
        PromptTemplate.builder()
            .setTemplate(template)
            .addAllVariableValuePairs(new HashMap<>(Map.of("name", "John", "age", "30")))
            .build()
            .format();
    // Assert.
    String expected =
        "This is a template for a prompt.\n"
            + "It can be used to test the prompt template.\n"
            + "Name: John\n"
            + "Age: 30\n";
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testVariableExtractor() {
    // Arrange.
    String template =
        "This is a template for a prompt.\n"
            + "It can be used to test the prompt template.\n"
            + "Name: {{$name}}\n"
            + "Age: {{$age}}\n";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(template);

    // Assert.
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.get(0)).isEqualTo("name");
    assertThat(actual.get(1)).isEqualTo("age");
  }

  @Test
  public void testExtractVariables_singleVariable() {
    // Arrange.
    String input = "I really love this {{$subject}}";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).hasSize(1);
    assertThat(actual.get(0)).isEqualTo("subject");
  }

  @Test
  public void testExtractVariables_multipleVariables() {
    // Arrange.
    String input = "I really love this {{$subject}} because the teacher is so {{$adj}}";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).hasSize(2);
    assertThat(actual.get(0)).isEqualTo("subject");
    assertThat(actual.get(1)).isEqualTo("adj");
  }

  @Test
  public void testExtractVariables_noVariables() {
    // Arrange.
    String input = "I really love this subject";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).isEmpty();
  }

  @Test
  public void testExtractVariables_emptyString() {
    // Arrange.
    String input = "";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).isEmpty();
  }
}
