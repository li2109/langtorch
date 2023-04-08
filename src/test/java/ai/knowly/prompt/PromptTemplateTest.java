package ai.knowly.prompt;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PromptTemplateTest {
  @Test
  public void testPromptTemplate() {
    // Arrange.
    String template =
        "This is a template for a prompt.\n"
            + "It can be used to test the prompt template.\n"
            + "Name: {{$name}}\n"
            + "Age: {{$age}}\n";

    // Act.
    Optional<String> actual =
        PromptTemplate.builder()
            .setTemplate(template)
            .setVariables(new HashMap<>(Map.of("name", "John", "age", "30")))
            .build()
            .format();
    // Assert.
    assertThat(actual.isPresent()).isTrue();
    String expected =
        "This is a template for a prompt.\n"
            + "It can be used to test the prompt template.\n"
            + "Name: John\n"
            + "Age: 30\n";
    assertThat(actual.get()).isEqualTo(expected);
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
    ImmutableList<String> actual =
        PromptTemplate.builder().setTemplate(template).build().extractVariableNames();

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
    ImmutableList<String> actual =
        PromptTemplate.builder().setTemplate(input).build().extractVariableNames();

    // Assert.
    assertThat(actual).hasSize(1);
    assertThat(actual.get(0)).isEqualTo("subject");
  }

  @Test
  public void testExtractVariables_multipleVariables() {
    // Arrange.
    String input = "I really love this {{$subject}} because the teacher is so {{$adj}}";

    // Act.
    ImmutableList<String> actual =
        PromptTemplate.builder().setTemplate(input).build().extractVariableNames();

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
    ImmutableList<String> actual =
        PromptTemplate.builder().setTemplate(input).build().extractVariableNames();

    // Assert.
    assertThat(actual).isEmpty();
  }

  @Test
  public void testExtractVariables_emptyString() {
    // Arrange.
    String input = "";

    // Act.
    ImmutableList<String> actual =
        PromptTemplate.builder().setTemplate(input).build().extractVariableNames();

    // Assert.
    assertThat(actual).isEmpty();
  }
}
