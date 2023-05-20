package ai.knowly.langtorch.prompt.template;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

final class PromptTemplateTest {
  @Test
  void promptTemplateWithReusableVariable() {
    // Arrange.
    PromptTemplate promptTemplate =
        PromptTemplate.builder()
            .setExampleHeader("Here's one example:")
            .setExamples(
                ImmutableList.of(
                    "What is 25% of 1400?\n"
                        + "a) 700\n"
                        + "b) 350\n"
                        + "c) 1050\n"
                        + "d) 1000\n"
                        + "answer: b"))
            .setTemplate(
                "Can you please generate a trivia question by following the response template:\n"
                    + "{question}\n"
                    + "{option A}\n"
                    + "{option B}\n"
                    + "{option C}\n"
                    + "{option D}\n"
                    + "{answer}")
            .build();

    // Act.
    // Assert.
    assertThat(promptTemplate.format())
        .isEqualTo(
            "Can you please generate a trivia question by following the response template:\n"
                + "{question}\n"
                + "{option A}\n"
                + "{option B}\n"
                + "{option C}\n"
                + "{option D}\n"
                + "{answer}\n"
                + "Here's one example:\n"
                + "What is 25% of 1400?\n"
                + "a) 700\n"
                + "b) 350\n"
                + "c) 1050\n"
                + "d) 1000\n"
                + "answer: b\n");
  }

  @Test
  public void promptTemplateWithSections() {
    // Arrange.
    PromptTemplate promptTemplate =
        PromptTemplate.builder().setTemplate("What's the stock price of {{$ticker}}?").build();

    // Act.
    // Assert.
    assertThat(promptTemplate.toBuilder().addVariableValuePair("ticker", "GOOG").build().format())
        .isEqualTo("What's the stock price of GOOG?");

    assertThat(promptTemplate.toBuilder().addVariableValuePair("ticker", "MSFT").build().format())
        .isEqualTo("What's the stock price of MSFT?");
  }

  @Test
  void testPromptTemplateExample() {
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
  void testPromptTemplateExample_withCustomizedExampleHeader() {
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
        "Provide me company names for the technology\n"
            + "Here's two examples:\n"
            + "Search Engine: Google\n"
            + "Social Media: Facebook\n";
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testPromptTemplate() {
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
            .addAllVariableValuePairs(new HashMap<>(ImmutableMap.of("name", "John", "age", "30")))
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
  void testVariableExtractor() {
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
  void testExtractVariables_singleVariable() {
    // Arrange.
    String input = "I really love this {{$subject}}";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).hasSize(1);
    assertThat(actual.get(0)).isEqualTo("subject");
  }

  @Test
  void testExtractVariables_multipleVariables() {
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
  void testExtractVariables_noVariables() {
    // Arrange.
    String input = "I really love this subject";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).isEmpty();
  }

  @Test
  void testExtractVariables_emptyString() {
    // Arrange.
    String input = "";

    // Act.
    ImmutableList<String> actual = PromptTemplate.extractVariableNames(input);

    // Assert.
    assertThat(actual).isEmpty();
  }
}
