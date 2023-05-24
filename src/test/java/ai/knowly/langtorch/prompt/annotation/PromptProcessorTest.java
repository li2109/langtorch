package ai.knowly.langtorch.prompt.annotation;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import ai.knowly.langtorch.prompt.template.PromptTemplate;
import org.junit.jupiter.api.Test;

final class PromptProcessorTest {
  @Test
  void testPromptProcessorWithMultiplePrompts() {
    // Arrange
    SubjectRelatedPrompt subjectRelatedPrompt = new SubjectRelatedPrompt();

    // Act
    PromptTemplate textPromptTemplate =
        PromptProcessor.createPromptTemplate(
            SubjectRelatedPrompt.class, subjectRelatedPrompt, "favoriteSubjectPrompt");
    PromptTemplate contactInfoPromptTemplate =
        PromptProcessor.createPromptTemplate(
            SubjectRelatedPrompt.class, subjectRelatedPrompt, "reasonPrompt");

    // Assert
    assertThat(textPromptTemplate.format()).isEqualTo("My favourite subject in school is Math.");
    assertThat(contactInfoPromptTemplate.format())
        .isEqualTo("I really love Math because I love numbers.");
  }

  @Test
  void testPromptProcessorWithNonExistentPromptName() {
    // Arrange
    SubjectRelatedPrompt subjectRelatedPrompt = new SubjectRelatedPrompt();

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class,
        () ->
            PromptProcessor.createPromptTemplate(
                SubjectRelatedPrompt.class, subjectRelatedPrompt, "nonExistentPromptName"));
  }

  @Test
  void testAnnotationBasedPromptTemplate() {
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
  void testPromptWithStringVariable() {
    // Arrange
    final String template = "Hello, {{$name}}! Your age is {{$age}}.";
    @Prompt(
        template = template,
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
  void testAnnotationBasedPromptTemplateNoVariables() {
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
  void testAnnotationBasedPromptTemplateInvalidVariable() {
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
  void testAnnotationBasedPromptTemplateMissingAnnotation() {
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

  @Test
  void testAnnotationBasedPromptTemplateWithExamples() {
    // Arrange
    @Prompt(
        template = "Hello, {{$name}}! Your age is {{$age}}.",
        variables = {"name", "age"},
        examples = {"Hello, John! Your age is 25.", "Hello, Alice! Your age is 29."})
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
    assertThat(result)
        .isEqualTo(
            "Hello, Jane! Your age is 30.\n"
                + "Here's examples:\n"
                + "Hello, John! Your age is 25.\n"
                + "Hello, Alice! Your age is 29.\n");
  }

  @Test
  void testAnnotationBasedPromptTemplateWithExamplesAndExampleHeader() {
    // Arrange
    @Prompt(
        template = "Hello, {{$name}}! Your age is {{$age}}.",
        variables = {"name", "age"},
        examples = {"Hello, John! Your age is 25.", "Hello, Alice! Your age is 29."},
        exampleHeader = "Example inputs:")
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
    assertThat(result)
        .isEqualTo(
            "Hello, Jane! Your age is 30.\n"
                + "Example inputs:\n"
                + "Hello, John! Your age is 25.\n"
                + "Hello, Alice! Your age is 29.\n");
  }

  @Prompt(
      template = "My favourite subject in school is {{$subject}}.",
      variables = {"subject"},
      name = "favoriteSubjectPrompt")
  @Prompt(
      template = "I really love {{$subject}} because {{$reason}}.",
      name = "reasonPrompt",
      variables = {"reason", "subject"})
  private static class SubjectRelatedPrompt {
    public String subject = "Math";
    public String reason = "I love numbers";
  }
}
