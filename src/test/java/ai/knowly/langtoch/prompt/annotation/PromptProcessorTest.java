package ai.knowly.langtoch.prompt.annotation;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PromptProcessorTest {
  @Test
  public void testAnnotationBasedPromptTemplate() {
    // Arrange
    @Prompt(
        template = "Hello, {{$name}}! Your age is {{$age}}.",
        variables = {"name", "age"})
    class HelloAgePrompt {
      public String name = "Jane";
      public String age = "30";
    }
    HelloAgePrompt helloAgePrompt = new HelloAgePrompt();

    // Act
    PromptTemplate helloAgePromptTemplate =
        PromptProcessor.createPromptTemplate(HelloAgePrompt.class, helloAgePrompt);
    String result = helloAgePromptTemplate.format();

    // Assert
    assertThat(result).isEqualTo("Hello, Jane! Your age is 30.");
  }

  @Test
  public void testAnnotationBasedPromptTemplateNoVariables() {
    // Arrange
    @Prompt(template = "Hello, world!")
    class HelloWorldPrompt {}
    HelloWorldPrompt helloWorldPrompt = new HelloWorldPrompt();

    // Act
    PromptTemplate helloWorldPromptTemplate =
        PromptProcessor.createPromptTemplate(HelloWorldPrompt.class, helloWorldPrompt);
    String result = helloWorldPromptTemplate.format();

    // Assert
    assertThat(result).isEqualTo("Hello, world!");
  }

  @Test
  public void testAnnotationBasedPromptTemplateInvalidVariable() {
    // Arrange
    @Prompt(
        template = "Hello, {{$name}}! Your age is {{$age}}.",
        variables = {"name"})
    class InvalidHelloAgePrompt {
      public String name = "Jane";
    }
    InvalidHelloAgePrompt invalidHelloAgePrompt = new InvalidHelloAgePrompt();

    // Act
    PromptTemplate invalidHelloAgePromptTemplate =
        PromptProcessor.createPromptTemplate(InvalidHelloAgePrompt.class, invalidHelloAgePrompt);

    // Assert
    assertThrows(IllegalArgumentException.class, invalidHelloAgePromptTemplate::format);
  }

  @Test
  public void testAnnotationBasedPromptTemplateMissingAnnotation() {
    // Arrange
    class NoAnnotation {
      public String name = "Jane";
      public String age = "30";
    }
    NoAnnotation noAnnotation = new NoAnnotation();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> PromptProcessor.createPromptTemplate(NoAnnotation.class, noAnnotation));
  }
}
