package ai.knowly.langtorch.connector.md;

import ai.knowly.langtorch.connector.DocumentConnector;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/** Implementation of DocumentConnector for Md files. */
public class MdConnector extends DocumentConnector<MdReadOption> {
    private MdConnector(){};

    public static MdConnector create(){
        return new MdConnector();
    }

    public String read(String filePath) throws IOException {
        return read(MdReadOption.builder().setFilePath(filePath).build());
    }

    @Override
    protected String read(MdReadOption readOption) throws IOException {
        // Read the Markdown file content
        return new String(Files.readAllBytes(Paths.get(readOption.getFilePath())));
    }

}
