package ai.knowly.langtorch.prompt.manager;

import ai.knowly.langtorch.prompt.template.PromptTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/** A class to manage prompt templates with multiple versions. */
public final class PromptManager {
  private static final Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(PromptTemplate.class, new PromptTemplateTypeAdapter())
          .create();
  private static final String DEFAULT_FILE_NAME = "prompt-manager.json";

  private final Map<Long, PromptTemplate> promptTemplateVersions;

  /**
   * Private constructor for PromptManager.
   *
   * @param promptTemplateVersions A map containing prompt templates and their version numbers.
   */
  private PromptManager(Map<Long, PromptTemplate> promptTemplateVersions) {
    this.promptTemplateVersions = promptTemplateVersions;
  }

  /**
   * Creates a new instance of PromptManager.
   *
   * @return A new instance of PromptManager.
   */
  public static PromptManager create() {
    return new PromptManager(new HashMap<>());
  }

  /**
   * Creates an instance of PromptManager from a JSON string.
   *
   * @param json The JSON string.
   * @return The instance of PromptManager.
   */
  private static PromptManager fromJson(String json) {
    PromptManagerConfig config = gson.fromJson(json, PromptManagerConfig.class);
    return new PromptManager(config.getPromptTemplates());
  }

  /**
   * Loads a PromptManager from a file with the default file name.
   *
   * @param folderName The folder name.
   * @return An instance of PromptManager.
   */
  public static PromptManager fromFile(String folderName) {
    return fromFile(folderName, DEFAULT_FILE_NAME);
  }

  /**
   * Loads a PromptManager from a file with a specified file name.
   *
   * @param folderName The folder name.
   * @param fileName The file name.
   * @return An instance of PromptManager.
   */
  public static PromptManager fromFile(String folderName, String fileName) {
    String path = String.format("%s/%s", folderName, fileName);
    try (FileInputStream inputStream = new FileInputStream(path)) {
      String json = IOUtils.toString(inputStream, Charset.defaultCharset());
      return fromJson(json);
    } catch (IOException e) {
      throw new FileLoadingException(e);
    }
  }

  /**
   * Saves the PromptManager to a file with the default file name.
   *
   * @param folderName The folder name.
   */
  public void toFile(String folderName) {
    toFile(folderName, DEFAULT_FILE_NAME);
  }

  /**
   * Saves the PromptManager to a file with a specified file name.
   *
   * @param folderName The folder name.
   * @param fileName The file name.
   */
  public void toFile(String folderName, String fileName) {
    String toWriteFileName = fileName.contains(".json") ? fileName : (fileName + ".json");
    try (FileWriter fileWriter = new FileWriter(folderName + "/" + toWriteFileName)) {
      fileWriter.write(toJson());
    } catch (IOException e) {
      throw new FileSaveException(e);
    }
  }

  /**
   * Converts the PromptManager to a JSON string.
   *
   * @return The JSON string.
   */
  private String toJson() {
    return gson.toJson(PromptManagerConfig.create(promptTemplateVersions));
  }

  /**
   * Returns the prompt template for a specific version.
   *
   * @param version The version number.
   * @return The PromptTemplate.
   */
  public PromptTemplate getPromptTemplate(long version) {
    return promptTemplateVersions.get(version);
  }

  /**
   * Checks if the PromptManager contains a specific version.
   *
   * @param version The version number.
   * @return A boolean indicating whether the version exists.
   */
  public boolean containsVersion(long version) {
    return promptTemplateVersions.containsKey(version);
  }

  /**
   * Adds a new prompt template with the specified version.
   *
   * @param version The version number.
   * @param promptTemplate The PromptTemplate to add.
   * @return The updated PromptManager instance.
   */
  public PromptManager addPromptTemplate(long version, PromptTemplate promptTemplate) {
    promptTemplateVersions.put(version, promptTemplate);
    return this;
  }

  /**
   * Removes a prompt template with the specified version.
   *
   * @param version The version number.
   */
  public void removePromptTemplate(long version) {
    promptTemplateVersions.remove(version);
  }

  /**
   * Updates a prompt template with the specified version.
   *
   * @param version The version number.
   * @param promptTemplate The updated PromptTemplate.
   */
  public void updatePromptTemplate(long version, PromptTemplate promptTemplate) {
    promptTemplateVersions.put(version, promptTemplate);
  }
}
