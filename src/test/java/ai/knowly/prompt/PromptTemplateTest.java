package ai.knowly.prompt;

import static com.google.common.truth.Truth.assertThat;

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
            + "Name: {name}\n"
            + "Age: {age}\n";

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
}
