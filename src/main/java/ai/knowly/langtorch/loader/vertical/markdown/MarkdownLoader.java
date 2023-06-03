package ai.knowly.langtorch.loader.vertical.markdown;

import ai.knowly.langtorch.loader.vertical.DocumentLoader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Implementation of DocumentConnector for Md files. */
public class MarkdownLoader extends DocumentLoader<MarkdownLoadOption> {
  private MarkdownLoader() {}

  public static MarkdownLoader create() {
    return new MarkdownLoader();
  }

  public String read(String filePath) throws IOException {
    return read(MarkdownLoadOption.builder().setFilePath(filePath).build());
  }

  @Override
  protected String read(MarkdownLoadOption readOption) throws IOException {
    // Read the Markdown file content
    return new String(
        Files.readAllBytes(Paths.get(readOption.getFilePath())), StandardCharsets.UTF_8);
  }
}
