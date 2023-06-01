package ai.knowly.langtorch.connector.markdown;

import ai.knowly.langtorch.connector.DocumentConnector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implementation of DocumentConnector for Md files.
 */
public class MarkdownConnector extends DocumentConnector<MarkdownReadOption> {
    private MarkdownConnector() {
    }

    public static MarkdownConnector create() {
        return new MarkdownConnector();
    }

    public String read(String filePath) throws IOException {
        return read(MarkdownReadOption.builder().setFilePath(filePath).build());
    }

    @Override
    protected String read(MarkdownReadOption readOption) throws IOException {
        // Read the Markdown file content
        return new String(Files.readAllBytes(Paths.get(readOption.getFilePath())), StandardCharsets.UTF_8);
    }

}
