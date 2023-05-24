package ai.knowly.langtorch.capability.local.v1;

import ai.knowly.langtorch.prompt.template.PromptTemplate;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;

/** A class for the capability. */
public class LocalCapabilityUnitReader {
  private static final String PROMPT_FILE_NAME = "prompt.txt";
  private static final String CONFIG_FILE_NAME = "config.json";
  private final String prompt;
  private final CapabilityConfig config;

  @Inject
  public LocalCapabilityUnitReader(String capabilityPath, Gson gson) {
    this.prompt = readFile(capabilityPath, TARGET.PROMPT);
    this.config = gson.fromJson(readFile(capabilityPath, TARGET.CONFIG), CapabilityConfig.class);
  }

  public String getPrompt() {
    return prompt;
  }

  public CapabilityConfig getConfig() {
    return config;
  }

  public PromptTemplate getPromptTemplate() {
    return PromptTemplate.builder().setTemplate(prompt).build();
  }

  private String readFile(String folderPath, TARGET target) {
    String fileName = target == TARGET.CONFIG ? CONFIG_FILE_NAME : PROMPT_FILE_NAME;
    String path = String.format("%s/%s", folderPath, fileName);
    try (FileInputStream inputStream = new FileInputStream(path)) {
      return IOUtils.toString(inputStream, Charset.defaultCharset());
    } catch (IOException e) {
      throw new LocalCapabilityReadException(e);
    }
  }

  private enum TARGET {
    CONFIG,
    PROMPT
  }
}
