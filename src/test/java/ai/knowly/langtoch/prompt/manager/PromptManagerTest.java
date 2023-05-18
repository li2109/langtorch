package ai.knowly.langtoch.prompt.manager;

import static com.google.common.truth.Truth.assertThat;

import ai.knowly.langtoch.prompt.template.PromptTemplate;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class PromptManagerTest {

  // Test adding a prompt template
  @Test
  void addPromptTemplateTest() {
    // Arrange.
    PromptManager promptManager = PromptManager.create();
    long version = 1;
    PromptTemplate promptTemplate = PromptTemplate.builder().setTemplate("Test").build();

    // Act.
    promptManager.addPromptTemplate(version, promptTemplate);

    // Assert.
    assertThat(promptManager.containsVersion(version)).isTrue();
    assertThat(promptManager.getPromptTemplate(version)).isEqualTo(promptTemplate);
  }

  // Test removing a prompt template
  @Test
  void removePromptTemplateTest() {
    // Arrange.
    PromptManager promptManager = PromptManager.create();
    long version = 1;
    PromptTemplate promptTemplate = PromptTemplate.builder().setTemplate("Test").build();

    // Act.
    promptManager.addPromptTemplate(version, promptTemplate);
    promptManager.removePromptTemplate(version);

    // Assert.
    assertThat(promptManager.containsVersion(version)).isFalse();
    assertThat(promptManager.getPromptTemplate(version)).isNull();
  }

  // Test updating a prompt template
  @Test
  public void updatePromptTemplateTest() {
    // Arrange.
    PromptManager promptManager = PromptManager.create();
    long version = 1;
    PromptTemplate promptTemplate1 = PromptTemplate.builder().setTemplate("Test1").build();
    PromptTemplate promptTemplate2 = PromptTemplate.builder().setTemplate("Test2").build();

    // Act.
    promptManager.addPromptTemplate(version, promptTemplate1);
    promptManager.updatePromptTemplate(version, promptTemplate2);

    // Assert.
    assertThat(promptManager.containsVersion(version)).isTrue();
    assertThat(promptManager.getPromptTemplate(version)).isEqualTo(promptTemplate2);
  }

  // Test saving and loading JSON
  @Test
  void saveAndLoadJsonTest() throws Exception {
    PromptManager promptManager = PromptManager.create();
    long version = 1;
    PromptTemplate promptTemplate = PromptTemplate.builder().setTemplate("Test").build();
    promptManager.addPromptTemplate(version, promptTemplate);

    Path tempDirectory = Files.createTempDirectory("promptManagerTest");
    String tempFolderPath = tempDirectory.toString();
    String fileName = "test-prompt-manager.json";

    // Save the configuration to JSON
    promptManager.toFile(tempFolderPath, fileName);

    // Load the configuration from JSON
    PromptManager loadedPromptManager = PromptManager.fromFile(tempFolderPath, fileName);

    // Check if the loaded configuration matches the original one
    assertThat(loadedPromptManager.containsVersion(version)).isTrue();
    assertThat(loadedPromptManager.getPromptTemplate(version)).isEqualTo(promptTemplate);

    // Clean up the temporary directory
    Files.deleteIfExists(new File(tempFolderPath, fileName).toPath());
    Files.deleteIfExists(tempDirectory);
  }

  // Test multi-version prompt templates
  @Test
  void multiVersionPromptTemplatesTest() {
    // Arrange.
    PromptManager promptManager = PromptManager.create();
    long version1 = 1;
    long version2 = 2;
    PromptTemplate promptTemplate1 = PromptTemplate.builder().setTemplate("Test1").build();
    PromptTemplate promptTemplate2 = PromptTemplate.builder().setTemplate("Test2").build();

    // Act.
    promptManager.addPromptTemplate(version1, promptTemplate1);
    promptManager.addPromptTemplate(version2, promptTemplate2);
    // Assert.
    assertThat(promptManager.containsVersion(version1)).isTrue();
    assertThat(promptManager.containsVersion(version2)).isTrue();
    assertThat(promptManager.getPromptTemplate(version1)).isEqualTo(promptTemplate1);
    assertThat(promptManager.getPromptTemplate(version2)).isEqualTo(promptTemplate2);
  }
}
